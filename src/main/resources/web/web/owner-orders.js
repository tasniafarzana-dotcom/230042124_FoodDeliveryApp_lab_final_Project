// ============================================================
// owner-orders.js — Module 6: Manage Orders & Assign Rider
// ============================================================

function renderOwnerOrdersSection() {
  document.getElementById("ownerSection").innerHTML += `
    <div class="box">
      <h3>📦 Manage Active Orders</h3>
      <input id="ownerRestaurantId" placeholder="Your Restaurant ID" />
      <button onclick="getOwnerOrders()">Load Orders</button>
      <div id="ownerLoadMsg" class="msg"></div>
      <ul id="ownerOrdersList"></ul>
    </div>

    <div class="box">
      <h3>✏️ Update Order</h3>
      <input id="ownerOrderId" placeholder="Order ID (Auto-filled)" />
      <select id="ownerStatus">
        <option value="CONFIRMED">CONFIRMED</option>
        <option value="PREPARING">PREPARING</option>
        <option value="OUT_FOR_DELIVERY">OUT_FOR_DELIVERY</option>
        <option value="DELIVERED">DELIVERED</option>
        <option value="CANCELLED">CANCELLED</option>
      </select>
      <button onclick="updateStatus()">Update Status</button>
      <button onclick="assignRider()" style="background:#f39c12;">Assign Rider</button>
      <div id="ownerMsg" class="msg"></div>
    </div>
  `;
}

async function getOwnerOrders() {
  const restId = document.getElementById("ownerRestaurantId").value;
  if (!restId) return showMsg("ownerLoadMsg", "❌ Enter your Restaurant ID", "error");
  showMsg("ownerLoadMsg", "⏳ Loading active orders...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:getActiveOrdersByRestaurant>
      <restaurantId>${restId}</restaurantId>
    </ws:getActiveOrdersByRestaurant></soapenv:Body></soapenv:Envelope>`;
  try {
    const doc  = await sendSoap(BASE + "/owner/orders", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("ownerOrdersList");
    ul.innerHTML = "";

    if (list.length === 0) return showMsg("ownerLoadMsg", "ℹ️ No active orders.", "info");
    showMsg("ownerLoadMsg", `✅ Found ${list.length} active order(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);
      if (data.id) {
        ul.innerHTML += `<li>
          <strong>Order ID:</strong> ${data.id}<br>
          <strong>Total:</strong> ৳${data.totalprice || 'N/A'}<br>
          <strong>Status:</strong> <span style="color:#e67e22;">${data.status || 'N/A'}</span><br>
          <button class="btn-select" onclick="autoSelectOrder('${data.id}')">Manage Order</button>
        </li>`;
      } else {
        ul.innerHTML += `<li><span style="color:#e74c3c;">⚠️ Missing ID!</span>
          <div class="diagnostic">${Object.keys(data).map(k=>`${k}:${data[k]}`).join(', ')}</div></li>`;
      }
    }
  } catch (err) { showMsg("ownerLoadMsg", "❌ " + err.message, "error"); }
}

async function updateStatus() {
  const orderId = document.getElementById("ownerOrderId").value;
  if (!orderId) return showMsg("ownerMsg", "❌ Enter Order ID", "error");
  showMsg("ownerMsg", "⏳ Updating status...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:updateOrderStatus>
      <orderId>${orderId}</orderId>
      <status>${document.getElementById("ownerStatus").value}</status>
    </ws:updateOrderStatus></soapenv:Body></soapenv:Envelope>`;
  try {
    await sendSoap(BASE + "/owner/orders", body);
    showMsg("ownerMsg", "✅ Status updated successfully!", "success");
    getOwnerOrders();
  } catch (err) { showMsg("ownerMsg", "❌ " + err.message, "error"); }
}

async function assignRider() {
  const orderId = document.getElementById("ownerOrderId").value;
  if (!orderId) return showMsg("ownerMsg", "❌ Enter Order ID", "error");
  showMsg("ownerMsg", "⏳ Assigning rider...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:assignRider><orderId>${orderId}</orderId></ws:assignRider>
    </soapenv:Body></soapenv:Envelope>`;
  try {
    await sendSoap(BASE + "/owner/orders", body);
    showMsg("ownerMsg", "✅ Rider assigned successfully!", "success");
  } catch (err) { showMsg("ownerMsg", "❌ " + err.message, "error"); }
}

window.autoSelectOrder = function(id) {
  document.getElementById("ownerOrderId").value = id;
  document.getElementById("ownerStatus").focus();
  document.getElementById("ownerOrderId").scrollIntoView({ behavior: "smooth", block: "center" });
}
