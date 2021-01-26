
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

object _autor extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.XmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.XmlFormat.Appendable]](play.twirl.api.XmlFormat) with _root_.play.twirl.api.Template1[Autor,play.twirl.api.XmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(autor:Autor):play.twirl.api.XmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""        """),format.raw/*2.9*/("""<?xml version="1.0" encoding="utf-8" ?>
<autor>
    <nombre>"""),_display_(/*4.14*/autor/*4.19*/.getNombre()),format.raw/*4.31*/("""</nombre>
    <descripcion>"""),_display_(/*5.19*/autor/*5.24*/.getApellidos()),format.raw/*5.39*/("""</descripcion>
    <ciudad_natal>autor.getCiudad_natal()</ciudad_natal>
</autor>"""))
      }
    }
  }

  def render(autor:Autor): play.twirl.api.XmlFormat.Appendable = apply(autor)

  def f:((Autor) => play.twirl.api.XmlFormat.Appendable) = (autor) => apply(autor)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2021-01-26T16:25:26.473434
                  SOURCE: /Users/eric/Google Drive/MASTER/CLOUD/REST/codigo/trabajo-eval/app/views/_autor.scala.xml
                  HASH: ed8dc284225fc1a4336c36cce8ed95c95d22036f
                  MATRIX: 901->1|1007->15|1041->23|1128->84|1141->89|1173->101|1227->129|1240->134|1275->149
                  LINES: 27->1|32->2|32->2|34->4|34->4|34->4|35->5|35->5|35->5
                  -- GENERATED --
              */
          