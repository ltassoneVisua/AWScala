package awscala.cloudwatch

import collection.JavaConverters._
import com.amazonaws.services.{ cloudwatch => aws }

object CloudWatchMetricStatisticResponse {

  def apply(obj: aws.model.GetMetricStatisticsResult): CloudWatchMetricStatisticResponse = CloudWatchMetricStatisticResponse(
    label = obj.getLabel,
    dataPoints = obj.getDatapoints.asScala.map(dp => CloudWatchDataPoint(dp)).toList
  )
}

case class CloudWatchMetricStatisticResponse(
  label: String,
  dataPoints: List[CloudWatchDataPoint]
)
    extends aws.model.GetMetricStatisticsResult {
}
