package progfun.config

sealed trait OutputFormat
object JSON extends OutputFormat
object CSV extends OutputFormat
object YAML extends OutputFormat
