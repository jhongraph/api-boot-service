from flask import Flask, render_template_string, request, jsonify
import subprocess
import threading
import os

app = Flask(__name__)
process_running = False
process_ref = None
logs = []

HTML = """
<!DOCTYPE html>
<html>
  <head>
    <title>Bot de Carga</title>
    <script>
      function loadLogs() {
        fetch('/logs')
          .then(response => response.json())
          .then(data => {
            const logBox = document.getElementById('log-box');
            logBox.innerText = data.logs.join('\\n');
            logBox.scrollTop = logBox.scrollHeight;
          });
      }

      function checkStatus() {
        fetch('/status')
          .then(response => response.json())
          .then(data => {
            const btnEjecutar = document.getElementById("btn-ejecutar");
            const btnDetener = document.getElementById("btn-detener");
            const estado = document.getElementById("estado");
            if (data.running) {
              btnEjecutar.disabled = true;
              btnDetener.disabled = false;
              estado.innerText = "🔄 Proceso en ejecución...";
            } else {
              btnEjecutar.disabled = false;
              btnDetener.disabled = true;
              estado.innerText = "🟢 Listo para ejecutar.";
            }
          });
      }

      function detenerProceso() {
        fetch('/stop', { method: 'POST' })
          .then(response => response.json())
          .then(data => {
            alert(data.message);
          });
      }

      setInterval(loadLogs, 2000);
      setInterval(checkStatus, 2000);
    </script>

    <style>
      body { text-align: center; padding-top: 30px; font-family: sans-serif; }
      #log-box {
        width: 90%;
        height: 400px;
        border-radius: 8px;
        padding: 15px;
        margin: 20px auto;
        overflow-y: auto;
        background-color: #000;
        color: #00ff00;
        font-family: 'Courier New', Courier, monospace;
        font-size: 14px;
        line-height: 1.4;
        box-shadow: 0 0 10px rgba(0, 255, 0, 0.3);
        white-space: pre-wrap;
        text-align: left;
      }
      input { padding: 5px; margin: 5px; width: 300px; }
      button { padding: 10px 20px; font-size: 16px; margin: 5px; }
    </style>
  </head>
  <body>
    <h1>🚀 Portal de Ejecución Manual</h1>
    <form method="POST">
      <input name="baseUrl" placeholder="URL base" required />
      <input name="ciclo" placeholder="Ciclo" type="number" required min="1"/>
      <input name="concurrency" placeholder="Concurrency" type="number" required min="1"/>
      <input name="waitTimeOut" placeholder="Wait Timeout" type="number" required min="1"/>
      <br/>
      <button id="btn-ejecutar" type="submit">Ejecutar Proceso</button>
      <button id="btn-detener" type="button" onclick="detenerProceso()">Detener Proceso</button>
    </form>
    <p id="estado">
      {% if running %}
        🔄 Proceso en ejecución...
      {% else %}
        🟢 Listo para ejecutar.
      {% endif %}
    </p>
    <div id="log-box">Cargando logs...</div>
  </body>
</html>
"""

@app.route("/", methods=["GET", "POST"])
def index():
    global process_running
    if request.method == "POST" and not process_running:
        base_url = request.form.get("baseUrl", "").strip()
        ciclo = request.form.get("ciclo", "").strip()
        concurrency = request.form.get("concurrency", "").strip()
        wait_timeout = request.form.get("waitTimeOut", "").strip()
        threading.Thread(target=run_process, args=(base_url, ciclo, concurrency, wait_timeout)).start()
    return render_template_string(HTML, running=process_running)

@app.route("/logs")
def get_logs():
    return jsonify({"logs": logs[-200:]})

@app.route("/status")
def get_status():
    return jsonify({"running": process_running})

@app.route("/stop", methods=["POST"])
def stop_process():
    global process_ref, process_running
    if process_ref and process_ref.poll() is None:
        try:
            process_ref.terminate()
            logs.append("🛑 Proceso detenido manualmente.")
            process_running = False
            return jsonify({"message": "Proceso detenido exitosamente."})
        except Exception as e:
            return jsonify({"message": f"Error al detener: {str(e)}"})
    else:
        return jsonify({"message": "No hay proceso en ejecución."})

def update_yaml_vars(base_url, ciclo, concurrency, wait_timeout):
    yaml_path = "/app/utilities/application.yaml"
    with open(yaml_path, "r") as f:
        content = f.read()

    replacements = {
        "VAR_URL": base_url,
        "VAR_CICLO": ciclo,
        "VAR_CONCURRENCY": concurrency,
        "VAR_WAITTIMEOUT": wait_timeout,
    }

    for placeholder, value in replacements.items():
        content = content.replace(placeholder, value)

    with open(yaml_path, "w") as f:
        f.write(content)

def run_process(base_url, ciclo, concurrency, wait_timeout):
    global process_running, process_ref, logs
    process_running = True
    try:
        logs.append(f"⚙️ Modificando application.yaml con nuevos valores...")
        update_yaml_vars(base_url, ciclo, concurrency, wait_timeout)
        logs.append("▶️ Ejecutando proceso...\n")
        process_ref = subprocess.Popen(
            ["java", "-jar", "/app/app.jar"],
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True
        )
        for line in process_ref.stdout:
            logs.append(line.strip())
        logs.append("✅ Proceso finalizado.\n")
    except Exception as e:
        logs.append(f"❌ ERROR: {str(e)}\n")
    finally:
        process_running = False

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
