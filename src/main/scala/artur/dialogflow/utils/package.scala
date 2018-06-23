package artur.dialogflow

package object utils {

  trait Configs {
    def getProjectId: String
  }


  case class BaseAgentConfigs(projectId: String) extends Configs {
    override def getProjectId: String = projectId
  }

  object LanguageCode extends Enumeration {
    type LanguageCode = String
    val English = "en-EN"
    val Russian = "ru-RU"
  }
}
