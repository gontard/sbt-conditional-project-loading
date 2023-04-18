val localProperties: Map[String, String] = {
  import java.util.Properties
  val props = new Properties()
  props.load(new java.io.FileInputStream("local.properties"))
  import scala.collection.JavaConverters._
  props.asScala.toMap
}

val moduleNames: List[String] = localProperties.getOrElse("modules", "").split(",").filter(_.nonEmpty).toList
val subprojects = new CompositeProject {
  override def componentProjects: Seq[Project] = {
    moduleNames.map(file).map { p =>
      Project(p.getName, p).settings(name := p.getName)
    }
  }
}

val projects: List[Project] = subprojects.componentProjects.toList
val modules: List[ClasspathDep[ProjectReference]] =
  projects.map(classpathDependency(_)(Project.projectToRef))


lazy val root = (project in file("."))
  .dependsOn(modules: _*)
  .aggregate(modules.map(_.project): _*)
  .settings(
    name := "sbt-conditional-project-loading"
  )
