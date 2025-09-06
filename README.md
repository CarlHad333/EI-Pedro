**Supervisory System | Integration Education – 2023**
**Overview**

This project demonstrates a rapid IoT prototype for near-real-time health monitoring. It streams simulated insulin data from an ESP32 microcontroller via Bluetooth, processes the data on Google Cloud Platform (GCP), and visualizes metrics in real time using JavaFX dashboards. MATLAB Simulink integration allows simulation-based monitoring with minimal latency.

**Features**:

- IoT Data Streaming: Sends simulated insulin data from ESP32 via Bluetooth.

- Cloud Processing: Data handled and stored using Google Cloud Platform services (GCP SQL, Cloud APIs).

- Real-Time Visualization: Interactive dashboards built with JavaFX for live monitoring of health metrics.

- Simulink Integration: MATLAB Simulink simulations feed near-live data into the dashboard with minimal latency.

- Rapid Prototyping: Complete system built and demonstrated in just four days.

**Technologies Used**:

- Hardware: ESP32 microcontroller

- Cloud: Google Cloud Platform (GCP SQL, Cloud APIs)

- Programming & Visualization: Java, JavaFX

- Simulation: MATLAB Simulink

- Communication: Bluetooth

**Usage**:

- Launch the ESP32 simulator.

- Start GCP backend services to process and store data.

- Open the JavaFX dashboard to monitor insulin and other simulated health metrics.

- Optionally, run Simulink simulations to test latency and response.

**Future Improvements**:

- Support for real patient data (with secure authentication).

- Integration with mobile apps for remote monitoring.

- Additional health metrics (heart rate, glucose, etc.)

**License**:

This project is for educational purposes and can be shared under MIT License.
