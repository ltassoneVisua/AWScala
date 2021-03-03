package awscala.ses

import com.amazonaws.services.simpleemail.model.SendEmailResult
import com.amazonaws.services.{ simpleemail => aws }

object SESSendEmailResult {

  def apply(obj: SendEmailResult): SESSendEmailResult = SESSendEmailResult(
    messageId = Option(obj.getMessageId)
  )
}

case class SESSendEmailResult(messageId: Option[String])
    extends aws.model.SendEmailResult {
}
