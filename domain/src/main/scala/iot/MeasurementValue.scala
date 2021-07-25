package iot

case class MeasurementKey(deviceId: Int)
case class MeasurementValue(id: Int, unit: String, value: Int)

// An idea is to simulate real time metrics of a driving car
// They are combined with weather info and turned into some driver notifications

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
