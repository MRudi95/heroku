package controllers

import java.util.UUID

import javax.inject._
import play.api.mvc._
import de.htwg.se.slay.SlayModule
import de.htwg.se.slay.controller.controllerComponent._
import de.htwg.se.slay.model.fileIOComponent.fileIoJSONimpl.FileIO
import de.htwg.se.slay.util.Observer
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import akka.actor._
import akka.stream.Materializer
import com.google.inject.{Guice, Injector}
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import utils.auth.DefaultEnv

import scala.util.Random

@Singleton
class HomeController @Inject()(cc: ControllerComponents, silhouette: Silhouette[DefaultEnv]) (implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
  val jsonIO = new FileIO
  val injector: Injector = Guice.createInjector(new SlayModule)

  val gameController = newGameInstance() //wird spaeter liste mit allen laufenden spielen
  var playerMap: Map[UUID, (ControllerInterface, String, Int, Option[MyWebSocketActor])] = Map()
  var latestPlayer: UUID = _ //cheaten um im websocket die id des spielers zu nutzen

  def newGameInstance():ControllerInterface = {
    val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

    controller.createGrid("Map1")

    controller.nextturn()

    controller
  }


  def startGame() = silhouette.SecuredAction { implicit request =>
//    println("OLLA")

    val game = newGameInstance()
    val gamecode = Random.alphanumeric.take(5).mkString("").toUpperCase
    val player = 1
    val playerid = request.identity.userID
    val playername = request.identity.firstName match{
      case Some(name) => name
      case None => "Player 1"
    }
    game.changeName(playername, player)
    game.nextturn() //set game to player 2 at first, so player 1 cant do stuff until player 2 is there

    playerMap = playerMap + ( playerid -> {(game, gamecode, player, None)} )
    latestPlayer = playerid
//    println(playerMap)

    Redirect("/assets/play/index.html")
  }

  def joinGame(code: String) = silhouette.SecuredAction { implicit request =>
    //check if code has right format (5 characters or digits)
    println(code)
    if(code.length != 5)
      InternalServerError("code has wrong length")
    else
    {
      val player  = 2
      val playerid = request.identity.userID
      val playername = request.identity.firstName match{
        case Some(name) => name
        case None => "Player 2"
      }

      //get the game you want to join
      val result = playerMap.filter( entry => entry._2._2.equals(code) )

      if(result.isEmpty)
        InternalServerError("code does not exist") //code is not in the map / does not exist
      else if(result.size > 1)
        InternalServerError("too many players") //2 players already connected to the game
      else
      {
        val game = result.head._2._1
        game.changeName(playername, player)

        latestPlayer = playerid
        playerMap = playerMap + ( playerid -> {(game, code, player, None)} )


        Redirect("/assets/play/index.html")
      }
    }
  }


  def processCommand(command: String, request: SecuredRequest[DefaultEnv, AnyContent]): Result ={
    try{
      //get infos of the player that send the command
      val (game, code, number, socket) = playerMap(request.identity.userID)

      //check if it is this players turn right now or not
      if (checkTurn(number, game))
        processInput(command, game)
      else
        socket.get.out ! Json.obj( "message" -> "It is not your turn right now!").toString()

      Ok("")
    } catch {
      case _: Throwable => InternalServerError("")
    }
  }

  //commands
  def buy(coord: String) = silhouette.SecuredAction { implicit request =>
      val command = "buy " + coord
      processCommand(command, request)
  }

  def mov(coord1: String, coord2: String) = silhouette.SecuredAction{ implicit request =>
    val command = "mov " + coord1 + " " + coord2
    processCommand(command, request)
  }

  def cmb(coord1: String, coord2: String) = silhouette.SecuredAction{ implicit request =>
    val command = "cmb " + coord1 + " " + coord2
    processCommand(command, request)
  }

  def plc(coord: String) = silhouette.SecuredAction{ implicit request =>
    val command = "plc " + coord
    processCommand(command, request)
  }

  def bal(coord: String) = silhouette.SecuredAction{ implicit request =>
    val command = "bal " + coord
    processCommand(command, request)
  }

  def undo() = silhouette.SecuredAction{ implicit request =>
    val command = "undo"
    processCommand(command, request)
  }

  def redo() = silhouette.SecuredAction{ implicit request =>
    val command = "redo"
    processCommand(command, request)
  }

  def end() = silhouette.SecuredAction{ implicit request =>
    val command = "end"
    processCommand(command, request)
  }

  def surrender() = silhouette.SecuredAction{ implicit request =>
    val command = "ff20"
    processCommand(command, request)
  }

  def getJson() = Action{
    var json = jsonIO.gridToJson(gameController.grid, gameController.players)
    json = json.+(("player", Json.obj(
      "playername" -> getPlayerturn(gameController),
      "playercolor" -> getPlayercolor(gameController),
    )))
    Ok(json)
  }

  def getPlayerturn(game: ControllerInterface) = {
    game.players(game.state).name
  }
  def getPlayercolor(game: ControllerInterface)={
    game.state match {
      case 1 => "yellow"
      case 2 => "green"
      case _ => ""
    }
  }


  //websocket
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out)
    }
  }

  object MyWebSocketActor {
    def props(out: ActorRef) = {
      Props(new MyWebSocketActor(out))
    }
  }

  class MyWebSocketActor(val out: ActorRef) extends Actor with Observer {
//    println("WS:" + playerMap.get(latestPlayer))
    val info = playerMap(latestPlayer)
    playerMap = playerMap + ( latestPlayer -> { info.copy(_4 = Option(this)) } ) //websocket des spielers in seine infos einfuegen

    val (game, gamecode, playernumber, socket) = info
    game.add(this)


    def receive = {
      //gets an initial message "SYN" from frontend
      case msg: String =>
        out ! ("I received your message: " + msg)
        //send the gamecode to player 1
        println(gamecode)
        if(playernumber == 1)
          out ! Json.obj( "code" -> gamecode).toString()
        if(playernumber == 2)
          game.nextturn() //set game to player 1 to start the game
    }

    val jsonIO = new FileIO
    override def update(e: Event): Boolean = {
      e match{
        case _: SuccessEvent => out ! jsonIO.gridToJson(game.grid, game.players).toString(); true
        case _: MoneyErrorEvent => out ! Json.obj( "message" -> "Not enough Money!").toString(); true
        case b: BalanceEvent =>
          val msg = "Balance: " + b.bal + " Income: " + b.inc + " ArmyCost: " + b.cost
          out ! Json.obj( "message" -> msg).toString(); true
        case _: OwnerErrorEvent => out ! Json.obj( "message" -> "You are not the Owner of this!").toString(); true
        case _: GamePieceErrorEvent => out ! Json.obj( "message" -> "There already is a GamePiece there!").toString(); true
        case _: CombineErrorEvent => out ! Json.obj( "message" -> "Can't combine those Units!").toString(); true
        case m: MoveErrorEvent =>
          val msg = "Can't move there! " + m.reason
          out ! Json.obj( "message" -> msg).toString(); true
        case _: MovableErrorEvent => out ! Json.obj( "message" -> "This Unit is not movable!").toString(); true
        case _: MovedErrorEvent => out ! Json.obj( "message" -> "This Unit has already moved this turn!").toString(); true
        case _: UndoErrorEvent => out ! Json.obj( "message" -> "Nothing to undo!").toString(); true
        case _: RedoErrorEvent => out ! Json.obj( "message" -> "Nothing to redo!").toString(); true
        case _: PlayerEvent =>
          out ! Json.obj("player" -> Json.obj(
            "playername" -> getPlayerturn(game),
            "playercolor" -> getPlayercolor(game),
          )).toString(); true
        case _ => false
      }
    }
  }

  //taken from TextUI
  def processInput(input: String, controller: ControllerInterface): Unit = {
    val coord = "[A-Z]\\d+".r
    val bal = s"bal ($coord)".r
    val buy = s"buy ($coord)".r
    val plc = s"plc ($coord)".r
    val mov = s"mov ($coord) ($coord)".r
    val cmb = s"cmb ($coord) ($coord)".r

    input match {
      case "q" =>
      case "quit" =>
      case "undo" => controller.undo()
      case "redo" => controller.redo()
      case "end" => controller.nextturn()
      case "ff20" => controller.surrender()
      case bal(c) if checkIndex(c) =>
        controller.seeBalance(convertIndex(c))
      case buy(c) if checkIndex(c) =>
        controller.buyPeasant(convertIndex(c))
      case plc(c) if checkIndex(c) =>
        controller.placeCastle(convertIndex(c))
      case mov(c1, c2) if checkIndex(c1) && checkIndex(c2) =>
        controller.moveUnit(convertIndex(c1), convertIndex(c2))
      case cmb(c1, c2) if checkIndex(c1) && checkIndex(c2) =>
        controller.combineUnit(convertIndex(c1), convertIndex(c2))
      case _ => println("Wrong Input!")
    }

    //helper functions
    def checkIndex(idx: String): Boolean ={
      val coord = "([A-Z])(\\d+)".r
      val coord(cols, rows) = idx
      val col = cols.charAt(0).toInt - 65
      if(col > controller.grid.colIdx || rows.toInt - 1 > controller.grid.rowIdx) false else true
    }
    def convertIndex(idx: String): Int ={
      val cols = idx.charAt(0).toInt - 65
      val rows = idx.charAt(1).asDigit - 1
      rows * (controller.grid.colIdx+1) + cols
    }
  }

  def checkTurn(playernumber: Int, controller: ControllerInterface ): Boolean ={
    controller.state == playernumber
  }
}


