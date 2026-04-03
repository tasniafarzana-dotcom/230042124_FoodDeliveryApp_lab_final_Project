// ============================================================
// rider.js — Rider Tasks & Active Deliveries
// ============================================================

function renderRiderSection() {
  document.getElementById("riderSection").innerHTML = `
    <div class="box">
      <h3>🕒 Pending Tasks</h3>
      <button type="button" onclick="loadPendingTasks(event)">Refresh Tasks</button>
      <div id="pendingTaskMsg" class="msg"></div>
      <ul id="pendingTasksList"></ul>
    </div>

    <div class="box">
      <h3>🚴 Active Deliveries</h3>
      <button type="button" onclick="loadActiveDeliveries(event)">Refresh Deliveries</button>
      <div id="activeDeliveryMsg" class="msg"></div>
      <ul id="activeDeliveriesList"></ul>
    </div>
  `;
}

async function loadPendingTasks(event) {
  if (event) event.preventDefault();

  if (!currentUser || !currentUser.id) {
    return showMsg("pendingTaskMsg", "❌ Please login first.", "error");
  }

  showMsg("pendingTaskMsg", "⏳ Loading pending tasks...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:getPendingTasksForRider>
        <riderId>${currentUser.id}</riderId>
      </ws:getPendingTasksForRider>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc  = await sendSoap(BASE + "/rider/tasks", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("pendingTasksList");
    ul.innerHTML = "";

    if (list.length === 0) {
      return showMsg("pendingTaskMsg", "ℹ️ No pending tasks found.", "info");
    }

    showMsg("pendingTaskMsg", `✅ Found ${list.length} pending task(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);

      ul.innerHTML += `<li>
        <strong>Assignment ID:</strong> ${data.id || "N/A"}<br>
        <strong>Order ID:</strong> ${data.orderid || "N/A"}<br>
        <strong>Status:</strong> ${data.status || "PENDING"}<br>
        <button type="button" class="btn-action" onclick="respondToTask('${data.id}', 'ACCEPTED', event)">Accept</button>
        <button type="button" class="btn-danger" onclick="respondToTask('${data.id}', 'REJECTED', event)">Reject</button>
      </li>`;
    }
  } catch (err) {
    showMsg("pendingTaskMsg", "❌ " + err.message, "error");
  }
}

async function respondToTask(assignmentId, response, event) {
  if (event) event.preventDefault();

  if (!assignmentId) {
    return showMsg("pendingTaskMsg", "❌ Assignment ID missing.", "error");
  }

  showMsg("pendingTaskMsg", `⏳ ${response === "ACCEPTED" ? "Accepting" : "Rejecting"} task...`, "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:respondToAssignment>
        <assignmentId>${assignmentId}</assignmentId>
        <response>${response}</response>
      </ws:respondToAssignment>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    await sendSoap(BASE + "/rider/tasks", body);
    showMsg("pendingTaskMsg", `✅ Task ${response === "ACCEPTED" ? "accepted" : "rejected"} successfully!`, "success");

    loadPendingTasks();
    loadActiveDeliveries();
  } catch (err) {
    showMsg("pendingTaskMsg", "❌ " + err.message, "error");
  }
}

async function loadActiveDeliveries(event) {
  if (event) event.preventDefault();

  if (!currentUser || !currentUser.id) {
    return showMsg("activeDeliveryMsg", "❌ Please login first.", "error");
  }

  showMsg("activeDeliveryMsg", "⏳ Loading active deliveries...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:getAcceptedOrdersForRider>
        <riderId>${currentUser.id}</riderId>
      </ws:getAcceptedOrdersForRider>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc  = await sendSoap(BASE + "/rider/tasks", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("activeDeliveriesList");
    ul.innerHTML = "";

    if (list.length === 0) {
      return showMsg("activeDeliveryMsg", "ℹ️ No active deliveries found.", "info");
    }

    const seen = new Set();
    let rendered = 0;

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);
      const orderId = data.id || "";

      if (!orderId || seen.has(orderId)) continue;
      seen.add(orderId);

      ul.innerHTML += `<li>
        <strong>Order ID:</strong> ${orderId}<br>
        <strong>Total:</strong> ৳${data.totalprice || "N/A"}<br>
        <strong>Status:</strong> ${data.status || "N/A"}<br>
        <strong>Address ID:</strong> ${data.deliveryaddressid || "DEFAULT"}<br>
        <strong>Assignment ID:</strong> ${data.assignmentid || "N/A"}<br>
        <button type="button" class="btn-action" onclick="markDelivered('${orderId}', event)">Mark as Delivered</button>
      </li>`;
      rendered++;
    }

    showMsg("activeDeliveryMsg", `✅ Found ${rendered} active deliver${rendered === 1 ? "y" : "ies"}.`, "success");
  } catch (err) {
    showMsg("activeDeliveryMsg", "❌ " + err.message, "error");
  }
}

async function markDelivered(orderId, event) {
  if (event) event.preventDefault();

  if (!orderId) {
    return showMsg("activeDeliveryMsg", "❌ Order ID missing.", "error");
  }

  showMsg("activeDeliveryMsg", "⏳ Completing delivery...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:completeDelivery>
        <orderId>${orderId}</orderId>
        <riderId>${currentUser.id}</riderId>
      </ws:completeDelivery>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/rider/tasks", body);
    const returns = getList(doc, "return");
    const msg = returns.length > 0
      ? returns[0].textContent.trim()
      : `✅ Order ${orderId} marked as delivered successfully!`;

    showMsg("activeDeliveryMsg", msg, "success");
    loadPendingTasks();
    loadActiveDeliveries();
  } catch (err) {
    showMsg("activeDeliveryMsg", "❌ " + err.message, "error");
  }
}