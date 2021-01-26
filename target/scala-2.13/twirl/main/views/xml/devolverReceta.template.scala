
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

object devolverReceta extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.XmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.XmlFormat.Appendable]](play.twirl.api.XmlFormat) with _root_.play.twirl.api.Template1[Receta,play.twirl.api.XmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(receta:Receta):play.twirl.api.XmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""        """),format.raw/*2.9*/("""<?xml version="1.0" encoding="utf-8" ?>
<receta>
    <nombre>"""),_display_(/*4.14*/receta/*4.20*/.getNombre()),format.raw/*4.32*/("""</nombre>
    <descripcion>"""),_display_(/*5.19*/receta/*5.25*/.getDescripcion()),format.raw/*5.42*/("""</descripcion>
</receta>"""))
      }
    }
  }

  def render(receta:Receta): play.twirl.api.XmlFormat.Appendable = apply(receta)

  def f:((Receta) => play.twirl.api.XmlFormat.Appendable) = (receta) => apply(receta)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2021-01-26T15:59:39.435685
                  SOURCE: /Users/eric/Google Drive/MASTER/CLOUD/REST/codigo/trabajo-eval/app/views/devolverReceta.scala.xml
                  HASH: 4356392c2d242d5edfe671fda288da48a85cea66
                  MATRIX: 910->1|1018->17|1052->25|1140->87|1154->93|1186->105|1240->133|1254->139|1291->156
                  LINES: 27->1|32->2|32->2|34->4|34->4|34->4|35->5|35->5|35->5
                  -- GENERATED --
              */
          