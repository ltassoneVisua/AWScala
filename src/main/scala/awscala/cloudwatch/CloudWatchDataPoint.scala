package awscala.cloudwatch

import com.amazonaws.services.cloudwatch.model.Datapoint
import com.amazonaws.services.{ cloudwatch => aws }
import org.joda.time.DateTime

object CloudWatchDataPoint {

  def apply(obj: Datapoint): CloudWatchDataPoint = CloudWatchDataPoint(
    timestamp = new DateTime(obj.getTimestamp),
    value = {
    if (obj.getSampleCount != null) {
      Option(obj.getSampleCount)
    } else if (obj.getAverage != null) {
      Option(obj.getAverage)
    } else if (obj.getSum != null) {
      Option(obj.getSum)
    } else if (obj.getMinimum != null) {
      Option(obj.getMinimum)
    } else {
      Option(obj.getMaximum)
    }
  },
    unit = obj.getUnit
  )
}

case class CloudWatchDataPoint(
  timestamp: DateTime,
  value: Option[Double],
  unit: String
)
    extends aws.model.Datapoint {
}
