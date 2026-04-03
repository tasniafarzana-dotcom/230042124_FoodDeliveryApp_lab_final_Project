// ============================================================
// rider.js — Module 7: Pending Tasks & Active Deliveries
// ============================================================

function renderRiderSection() {
  document.getElementById("riderSection").innerHTML = `
    <div class="box">
      <h3>⏳ Pending Tasks (New Assignments)</h3>
      <button onclick="loadPendingTasks()">🔄 Refresh Tasks</button>
      <div id="pendingTaskMsg" class="msg"></div>
      <ul id="pendingTasksList"></ul>
    </div>

    <div class="box">
      <h3>🚴 Active Deliveries</h3>
      <button onclick="loadActiveDeliveries()">🔄 Refresh Deliveries</button>
      <div id="activeDeliveryMsg" class="msg"></div>
      <ul id="activeDeliveriesList"></ul>
    </div>
  `;
}

async function loadPendingTasks() {
  showMsg("pendingTaskMsg", "⏳ Loading pending tasks...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:getPendingTasksForRider>
      <riderId>${currentUser.id}</riderId>
    </ws:getPendingTasksForRider></soapenv:Body></soapenv:Envelope>`;
  try {
    const doc  = await sendSoap(BASE + "/rider/tasks", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("pendingTasksList");
    ul.innerHTML = "";

    if (list.length === 0) return showMsg("pendingTaskMsg", "ℹ️ No pending tasks right now.", "info");
    showMsg("pendingTaskMsg", `✅ ${list.length} pending task(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);
      if (data.id) {
        ul.innerHTML += `<li style="border-left-color:#8e44ad;">
          <strong>Assignment ID:</strong> ${data.id}<br>
          <strong>Order ID:</strong> ${data.orderid || 'N/A'}<br>
          <strong>Assigned At:</strong> ${(data.assignedat||'').substring(11,19) || 'N/A'}<br>
          <button class="btn-action"  onclick="respondToTask('${data.id}','ACCEPTED')">✅ Accept</button>
          <button class="btn-danger" onclick="respondToTask('${data.id}','REJECTED')">❌ Reject</button>
        </li>`;
      }
    }
  } catch (err) { showMsg("pendingTaskMsg", "❌ " + err.message, "error"); }
}

async function respondToTask(assignmentId, response) {
  showMsg("pendingTaskMsg", `⏳ ${response === 'ACCEPTED' ? 'Accepting' : 'Rejecting'} task...`, "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:respondToAssignment>
      <assignmentId>${assignmentId}</assignmentId>
      <response>${response}</response>
    </ws:respondToAssignment></soapenv:Body></soapenv:Envelope>`;
  try {
    await sendSoap(BASE + "/rider/tasks", body);
    showMsg("pendingTaskMsg",
      response === 'ACCEPTED' ? "✅ Task accepted! Check Active Deliveries." : "Task rejected.",
      response === 'ACCEPTED' ? "success" : "info");
    loadPendingTasks();
    if (response === 'ACCEPTED') loadActiveDeliveries();
  } catch (err) { showMsg("pendingTaskMsg", "❌ " + err.message, "error"); }
}

async function loadActiveDeliveries() {
  showMsg("activeDeliveryMsg", "⏳ Loading active deliveries...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:getAcceptedTasksForRider>
      <riderId>${currentUser.id}</riderId>
    </ws:getAcceptedTasksForRider></soapenv:Body></soapenv:Envelope>`;
  try {
    const doc  = await sendSoap(BASE + "/rider/tasks", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("activeDeliveriesList");
    ul.innerHTML = "";

    if (list.length === 0) return showMsg("activeDeliveryMsg", "ℹ️ No active deliveries.", "info");
    showMsg("activeDeliveryMsg", `✅ ${list.length} active delivery(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);
      if (data.id || data.orderid) {
        ul.innerHTML += `<li style="border-left-color:#27ae60;">
          <strong>Order ID:</strong> ${data.orderid || data.id}<br>
          <strong>Amount:</strong> ৳${data.totalprice || 'N/A'}<br>
          <strong>Address:</strong> ${data.deliveryaddressid || 'N/A'}<br>
          <strong>Status:</strong> <span style="color:#27ae60;">${data.status || 'IN PROGRESS'}</span><br>
          <button class="btn-action" onclick="markDelivered('${data.orderid || data.id}')">✅ Mark as Delivered</button>
        </li>`;
      }
    }
  } catch (err) { showMsg("activeDeliveryMsg", "❌ " + err.message, "error"); }
}

async function markDelivered(orderId) {
  showMsg("activeDeliveryMsg", "⏳ Marking as delivered...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:updateOrderStatus>
      <orderId>${orderId}</orderId><status>DELIVERED</status>
    </ws:updateOrderStatus></soapenv:Body></soapenv:Envelope>`;
  try {
    await sendSoap(BASE + "/owner/orders", body);
    showMsg("activeDeliveryMsg", "✅ Order marked as delivered!", "success");
    loadActiveDeliveries();
  } catch (err) { showMsg("activeDeliveryMsg", "❌ " + err.message, "error"); }
}
