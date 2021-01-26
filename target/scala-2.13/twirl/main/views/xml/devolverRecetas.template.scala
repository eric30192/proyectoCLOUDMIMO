
package views.xml

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.xml._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.data._
import play.core.j.PlayFormsMagicForJava._
import scala.jdk.CollectionConverters._

object devolverRecetas extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.XmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.XmlFormat.Appendable]](play.twirl.api.XmlFormat) with _root_.play.twirl.api.Template1[List[Receta],play.twirl.api.XmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(recetas:List[Receta]):play.twirl.api.XmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""        """),format.raw/*2.9*/("""<?xml version="1.0" encoding="utf-8" ?>
        """),_display_(/*3.10*/for(r <- recetas) yield /*3.27*/{_display_(Seq[Any](format.raw/*3.28*/("""
            """),format.raw/*4.13*/("""<receta>
            <nombre>"""),_display_(/*5.22*/r/*5.23*/.getNombre()),format.raw/*5.35*/("""</nombre>
            <descripcion>"""),_display_(/*6.27*/r/*6.28*/.getDescripcion()),format.raw/*6.45*/("""</descripcion>
            """),_display_(/*7.14*/if(r.getAutor() != null)/*7.38*/{_display_(Seq[Any](format.raw/*7.39*/("""
            """),_display_(/*8.14*/_autor(r.getAutor())),format.raw/*8.34*/("""
            """)))}),format.raw/*9.14*/("""
            """),_display_(/*10.14*/if(r.getTipo() != null)/*10.37*/{_display_(Seq[Any](format.raw/*10.38*/("""
            """),_display_(/*11.14*/_tipo(r.getTipo())),format.raw/*11.32*/("""
            """)))}),format.raw/*12.14*/("""
            """),_display_(/*13.14*/if(r.getIngredientes().size() > 0)/*13.48*/{_display_(Seq[Any](format.raw/*13.49*/("""
            """),format.raw/*14.13*/("""<ingredientes>
                """),_display_(/*15.18*/for(i <- r.getIngredientes()) yield /*15.47*/{_display_(Seq[Any](format.raw/*15.48*/("""
                """),_display_(/*16.18*/_ingrediente(i)),format.raw/*16.33*/("""
                """)))}),format.raw/*17.18*/("""
            """),format.raw/*18.13*/("""</ingredientes>
            """)))}),format.raw/*19.14*/("""

        """)))}),format.raw/*21.10*/("""

"""),format.raw/*23.1*/("""</receta>"""))
      }
    }
  }

  def render(recetas:List[Receta]): play.twirl.api.XmlFormat.Appendable = apply(recetas)

  def f:((List[Receta]) => play.twirl.api.XmlFormat.Appendable) = (recetas) => apply(recetas)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2021-01-26T17:27:18.482201
                  SOURCE: /Users/eric/Google Drive/MASTER/CLOUD/REST/codigo/trabajo-eval/app/views/devolverRecetas.scala.xml
                  HASH: bd7c5979d93cd8ac5dcd09d23681bf45a534327b
                  MATRIX: 917->1|1032->24|1066->32|1141->81|1173->98|1211->99|1251->112|1307->142|1316->143|1348->155|1410->191|1419->192|1456->209|1510->237|1542->261|1580->262|1620->276|1660->296|1704->310|1745->324|1777->347|1816->348|1857->362|1896->380|1941->394|1982->408|2025->442|2064->443|2105->456|2164->488|2209->517|2248->518|2293->536|2329->551|2378->569|2419->582|2479->611|2521->622|2550->624
                  LINES: 27->1|32->2|32->2|33->3|33->3|33->3|34->4|35->5|35->5|35->5|36->6|36->6|36->6|37->7|37->7|37->7|38->8|38->8|39->9|40->10|40->10|40->10|41->11|41->11|42->12|43->13|43->13|43->13|44->14|45->15|45->15|45->15|46->16|46->16|47->17|48->18|49->19|51->21|53->23
                  -- GENERATED --
              */
          