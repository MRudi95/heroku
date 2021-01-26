
package views.html

/*1.2*/import play.api.i18n.Messages
/*2.2*/import play.api.mvc.RequestHeader
/*3.2*/import org.webjars.play.WebJarsUtil
/*4.2*/import controllers.AssetsFinder
/*5.2*/import play.api.data._
/*6.2*/import forms.TotpSetupForm.Data
/*7.2*/import com.mohiva.play.silhouette.impl.providers.GoogleTotpCredentials
/*8.2*/import com.mohiva.play.silhouette.impl.providers.GoogleTotpInfo

/**/
class home @javax.inject.Inject() /*10.6*/()(implicit webJarsUtil: WebJarsUtil, assets: AssetsFinder) extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template5[models.User,Option[GoogleTotpInfo],Option[scala.Tuple2[Form[Data], GoogleTotpCredentials]],RequestHeader,Messages,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*11.2*/(user: models.User, totpInfoOpt: Option[GoogleTotpInfo], totpDataOpt: Option[(Form[Data], GoogleTotpCredentials)] = None)(implicit request: RequestHeader, messages: Messages):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {

implicit def /*13.2*/implicitFieldConstructor/*13.26*/ = {{
    b4.vertical.fieldConstructor()
}};
Seq[Any](format.raw/*12.1*/("""
"""),format.raw/*15.2*/("""

"""),_display_(/*17.2*/main(messages("home.title"), Some(user))/*17.42*/ {_display_(Seq[Any](format.raw/*17.44*/("""
    """),format.raw/*18.5*/("""<div class="user col-md-6 col-md-offset-3">
        <div class="row">
            <hr class="col-md-12" />
            <h4 class="col-md-8">"""),_display_(/*21.35*/messages("welcome.signed.in")),format.raw/*21.64*/("""</h4>
            <div class="col-md-4 text-right">
                <img src=""""),_display_(/*23.28*/user/*23.32*/.avatarURL.getOrElse(assets.path("images/silhouette.png"))),format.raw/*23.90*/("""" height="40px" />
            </div>
            <hr class="col-md-12" />
        </div>
        <div class="row data">
            <div class="col-md-12">
                <div class="row">
                    <p class="col-md-6"><strong>"""),_display_(/*30.50*/messages("first.name")),format.raw/*30.72*/("""
                        """),format.raw/*31.25*/(""":</strong></p><p class="col-md-6">"""),_display_(/*31.60*/user/*31.64*/.firstName.getOrElse("None")),format.raw/*31.92*/("""</p>
                </div>
                <div class="row">
                    <p class="col-md-6"><strong>"""),_display_(/*34.50*/messages("last.name")),format.raw/*34.71*/("""
                        """),format.raw/*35.25*/(""":</strong></p><p class="col-md-6">"""),_display_(/*35.60*/user/*35.64*/.lastName.getOrElse("None")),format.raw/*35.91*/("""</p>
                </div>
                <div class="row">
                    <p class="col-md-6"><strong>"""),_display_(/*38.50*/messages("full.name")),format.raw/*38.71*/("""
                        """),format.raw/*39.25*/(""":</strong></p><p class="col-md-6">"""),_display_(/*39.60*/user/*39.64*/.fullName.getOrElse("None")),format.raw/*39.91*/("""</p>
                </div>
                <div class="row">
                    <p class="col-md-6"><strong>"""),_display_(/*42.50*/messages("email")),format.raw/*42.67*/("""
                        """),format.raw/*43.25*/(""":</strong></p><p class="col-md-6">"""),_display_(/*43.60*/user/*43.64*/.email.getOrElse("None")),format.raw/*43.88*/("""</p>
                </div>
                <div class="row">
                    <button type="button" class="btn btn-primary"><a href="/assets/play/index.html#/" style="color: white">Go to lobby!</a></button>
                </div>
            </div>
        </div>
        <div class="row data">
        """),_display_(/*51.10*/if(totpInfoOpt.nonEmpty)/*51.34*/ {_display_(Seq[Any](format.raw/*51.36*/("""
            """),format.raw/*52.13*/("""<h2>"""),_display_(/*52.18*/messages("totp.enabled.title")),format.raw/*52.48*/("""</h2>
            <a href=""""),_display_(/*53.23*/controllers/*53.34*/.routes.TotpController.disableTotp),format.raw/*53.68*/("""">
                <input type="button" class="btn btn-primary" value='"""),_display_(/*54.70*/messages("totp.disable")),format.raw/*54.94*/("""'/>
            </a>
        """)))}/*56.11*/else/*56.16*/{_display_(Seq[Any](format.raw/*56.17*/("""
            """),_display_(/*57.14*/totpDataOpt/*57.25*/ match/*57.31*/ {/*58.17*/case Some((totpForm, credentials)) =>/*58.54*/ {_display_(Seq[Any](format.raw/*58.56*/("""
                    """),format.raw/*59.21*/("""<h2>"""),_display_(/*59.26*/messages("totp.enabling.title")),format.raw/*59.57*/("""</h2>
                    <h2>"""),_display_(/*60.26*/messages("totp.shared.key.title")),format.raw/*60.59*/("""</h2>
                    <img src=""""),_display_(/*61.32*/credentials/*61.43*/.qrUrl),format.raw/*61.49*/(""""/>
                    <h2>"""),_display_(/*62.26*/messages("totp.recovery.tokens.title")),format.raw/*62.64*/("""</h2>
                    <ul>
                    """),_display_(/*64.22*/for(scratchCodePlain <- credentials.scratchCodesPlain) yield /*64.76*/ {_display_(Seq[Any](format.raw/*64.78*/("""
                        """),format.raw/*65.25*/("""<li>"""),_display_(/*65.30*/{
                            scratchCodePlain
                        }),format.raw/*67.26*/("""</li>
                    """)))}),format.raw/*68.22*/("""
                    """),format.raw/*69.21*/("""</ul>
                    """),_display_(/*70.22*/helper/*70.28*/.form(action = controllers.routes.TotpController.enableTotpSubmit())/*70.96*/ {_display_(Seq[Any](format.raw/*70.98*/("""
                        """),_display_(/*71.26*/helper/*71.32*/.CSRF.formField),format.raw/*71.47*/("""
                        """),_display_(/*72.26*/b4/*72.28*/.text(totpForm("verificationCode"), Symbol("_hiddenLabel") -> messages("totp.verification.code    "), Symbol("placeholder") -> messages("totp.verification.code"), Symbol("autocomplete") -> "off", Symbol("class") -> "form-control input-lg")),format.raw/*72.267*/("""
                        """),_display_(/*73.26*/b4/*73.28*/.hidden(totpForm("sharedKey"))),format.raw/*73.58*/("""
                        """),_display_(/*74.26*/helper/*74.32*/.repeat(totpForm("scratchCodes"), min = 1)/*74.74*/ { scratchCodeField =>_display_(Seq[Any](format.raw/*74.96*/("""
                            """),_display_(/*75.30*/b4/*75.32*/.hidden(scratchCodeField("hasher"))),format.raw/*75.67*/("""
                            """),_display_(/*76.30*/b4/*76.32*/.hidden(scratchCodeField("password"))),format.raw/*76.69*/("""
                            """),_display_(/*77.30*/b4/*77.32*/.hidden(scratchCodeField("salt"))),format.raw/*77.65*/("""
                        """)))}),format.raw/*78.26*/("""
                        """),format.raw/*79.25*/("""<div class="form-group">
                            <div>
                                <button id="submit" type="submit" value="submit" class="btn btn-lg btn-primary btn-block">"""),_display_(/*81.124*/messages("totp.verify")),format.raw/*81.147*/("""</button>
                            </div>
                        </div>
                    """)))}),format.raw/*84.22*/("""
                """)))}/*86.17*/case None =>/*86.29*/ {_display_(Seq[Any](format.raw/*86.31*/("""
                    """),format.raw/*87.21*/("""<h2>"""),_display_(/*87.26*/messages("totp.disabled.title")),format.raw/*87.57*/("""</h2>
                    <a href=""""),_display_(/*88.31*/controllers/*88.42*/.routes.TotpController.enableTotp),format.raw/*88.75*/("""">
                        <input type="button" class="btn btn-primary" value='"""),_display_(/*89.78*/messages("totp.enable")),format.raw/*89.101*/("""'/>
                    </a>
                """)))}}),format.raw/*92.14*/("""
        """)))}),format.raw/*93.10*/("""
        """),format.raw/*94.9*/("""</div>
    </div>
""")))}),format.raw/*96.2*/("""
"""))
      }
    }
  }

  def render(user:models.User,totpInfoOpt:Option[GoogleTotpInfo],totpDataOpt:Option[scala.Tuple2[Form[Data], GoogleTotpCredentials]],request:RequestHeader,messages:Messages): play.twirl.api.HtmlFormat.Appendable = apply(user,totpInfoOpt,totpDataOpt)(request,messages)

  def f:((models.User,Option[GoogleTotpInfo],Option[scala.Tuple2[Form[Data], GoogleTotpCredentials]]) => (RequestHeader,Messages) => play.twirl.api.HtmlFormat.Appendable) = (user,totpInfoOpt,totpDataOpt) => (request,messages) => apply(user,totpInfoOpt,totpDataOpt)(request,messages)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2021-01-21T18:19:08.579041
                  SOURCE: /home/lin/heroku/play-silhouette-seed/app/views/home.scala.html
                  HASH: f1e187fdf4597012be0077fe7977e26837781bf7
                  MATRIX: 28->1|65->32|106->67|149->104|188->137|218->161|257->194|335->266|447->336|905->397|1167->574|1200->598|1272->572|1300->640|1329->643|1378->683|1418->685|1450->690|1618->831|1668->860|1774->939|1787->943|1866->1001|2133->1241|2176->1263|2229->1288|2291->1323|2304->1327|2353->1355|2491->1466|2533->1487|2586->1512|2648->1547|2661->1551|2709->1578|2847->1689|2889->1710|2942->1735|3004->1770|3017->1774|3065->1801|3203->1912|3241->1929|3294->1954|3356->1989|3369->1993|3414->2017|3749->2325|3782->2349|3822->2351|3863->2364|3895->2369|3946->2399|4001->2427|4021->2438|4076->2472|4175->2544|4220->2568|4269->2599|4282->2604|4321->2605|4362->2619|4382->2630|4397->2636|4408->2655|4454->2692|4494->2694|4543->2715|4575->2720|4627->2751|4685->2782|4739->2815|4803->2852|4823->2863|4850->2869|4906->2898|4965->2936|5044->2988|5114->3042|5154->3044|5207->3069|5239->3074|5332->3146|5390->3173|5439->3194|5493->3221|5508->3227|5585->3295|5625->3297|5678->3323|5693->3329|5729->3344|5782->3370|5793->3372|6054->3611|6107->3637|6118->3639|6169->3669|6222->3695|6237->3701|6288->3743|6348->3765|6405->3795|6416->3797|6472->3832|6529->3862|6540->3864|6598->3901|6655->3931|6666->3933|6720->3966|6777->3992|6830->4017|7040->4199|7085->4222|7213->4319|7250->4354|7271->4366|7311->4368|7360->4389|7392->4394|7444->4425|7507->4461|7527->4472|7581->4505|7688->4585|7733->4608|7811->4668|7852->4678|7888->4687|7937->4706
                  LINES: 4->1|5->2|6->3|7->4|8->5|9->6|10->7|11->8|14->10|17->11|21->13|21->13|24->12|25->15|27->17|27->17|27->17|28->18|31->21|31->21|33->23|33->23|33->23|40->30|40->30|41->31|41->31|41->31|41->31|44->34|44->34|45->35|45->35|45->35|45->35|48->38|48->38|49->39|49->39|49->39|49->39|52->42|52->42|53->43|53->43|53->43|53->43|61->51|61->51|61->51|62->52|62->52|62->52|63->53|63->53|63->53|64->54|64->54|66->56|66->56|66->56|67->57|67->57|67->57|67->58|67->58|67->58|68->59|68->59|68->59|69->60|69->60|70->61|70->61|70->61|71->62|71->62|73->64|73->64|73->64|74->65|74->65|76->67|77->68|78->69|79->70|79->70|79->70|79->70|80->71|80->71|80->71|81->72|81->72|81->72|82->73|82->73|82->73|83->74|83->74|83->74|83->74|84->75|84->75|84->75|85->76|85->76|85->76|86->77|86->77|86->77|87->78|88->79|90->81|90->81|93->84|94->86|94->86|94->86|95->87|95->87|95->87|96->88|96->88|96->88|97->89|97->89|99->92|100->93|101->94|103->96
                  -- GENERATED --
              */
          