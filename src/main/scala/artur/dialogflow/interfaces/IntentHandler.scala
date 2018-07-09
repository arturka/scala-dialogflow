package artur.dialogflow.interfaces

import artur.dialogflow.utils.LanguageCode.LanguageCode
import com.google.cloud.dialogflow.v2.Intent
import com.google.cloud.dialogflow.v2.Intent.Message
import com.google.protobuf.{Struct, Value}

import scala.util.Try

trait IntentHandler {

  def batchIntentUpload(displayName: Option[String],
                        actionName: Option[String],
                        trainingPhrases: Option[Seq[String]],
                        payload: Option[Struct],
                        responseTexts: Option[Seq[String]],
                        languageCode: LanguageCode): Try[Intent]

}
