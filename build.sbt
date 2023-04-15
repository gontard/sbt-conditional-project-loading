lazy val module1 = (project in file("module1")).settings(name := "module1")
lazy val module2 = (project in file("module2")).settings(name := "module2")
lazy val module3 = (project in file("module3")).settings(name := "module3")

lazy val root = (project in file("."))
  .dependsOn(module1, module2, module3)
  .aggregate(module1, module2, module3)
  .settings(
    name := "sbt-conditional-project-loading"
  )

/*
NAIVE APPROACH - DOES NOT WORK

val moduleNames: List[String] = sys.env.getOrElse("MODULES", "").split(",").filter(_.nonEmpty).toList
val modules: List[ClasspathDep[ProjectReference]] = moduleNames.map { moduleName =>
  val module: Project = (project in file(moduleName)).settings(name := moduleName)
  classpathDependency(module)
}

lazy val root = (project in file("."))
  .dependsOn(modules: _*)
  .aggregate(modules.map(_.project): _*)
  .settings(
    name := "sbt-conditional-project-loading"
  )
*/
