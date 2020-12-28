// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/eric/Google Drive/MASTER/CLOUD/REST/codigo/trabajo-eval/conf/routes
// @DATE:Mon Dec 28 10:24:48 CET 2020


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
