package object car {
  object CarMetric extends Enumeration {
    val Speed, EngineRpm, TirePressure, BrakeFluidPressure = Value
  }

  case class CarMetricKey(carId: Int, metric: CarMetric.Value)
  case class CarMetricValue(value: Int)

  case class CarLocationKey(carId: Int)
  case class CarLocationValue(city: String)

  case class WeatherKey(city: String)
  case class WeatherValue(temperature: Int, isStorming: Boolean)

  case class DriverNotificationKey(carId: Int)
  case class DriverNotificationValue(msg: String)
}
