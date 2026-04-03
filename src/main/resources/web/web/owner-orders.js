// ============================================================
// owner-orders.js — Manage Orders & Assign Rider
// ============================================================

function renderOwnerOrdersSection() {
  document.getElementById("ownerSection").innerHTML += `
    <div class="box">
      <h3>📦 Manage Active Orders</h3>
      <input id="ownerRestaurantId" placeholder="Your Restaurant ID" />
      <button type="button" onclick="getOwnerOrders(event)">Load Orders</button>
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

      <select id="riderSelect">
        <option value="">Select Available Rider</option>
      </select>

      <button type="button" onclick="loadAvailableRiders(event)">Load Riders</button>
      <button type="button" onclick="updateStatus(event)">Update Status</button>
      <button type="button" onclick="assignRider(event)" style="background:#f39c12;">Assign Rider</button>
      <div id="ownerMsg" class="msg"></div>
    </div>
  `;
}

async function getOwnerOrders(event) {
  if (event) event.preventDefault();

  const restId = document.getElementById("ownerRestaurantId").value.trim();

  if (!restId) {
    return showMsg("ownerLoadMsg", "❌ Enter your Restaurant ID", "error");
  }

  saveSelectedRestaurantId(restId);
  showMsg("ownerLoadMsg", "⏳ Loading active orders...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:getActiveOrdersByRestaurant>
        <restaurantId>${restId}</restaurantId>
      </ws:getActiveOrdersByRestaurant>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc  = await sendSoap(BASE + "/owner/orders", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("ownerOrdersList");
    ul.innerHTML = "";

    if (list.length === 0) {
      return showMsg("ownerLoadMsg", "ℹ️ No active orders.", "info");
    }

    showMsg("ownerLoadMsg", `✅ Found ${list.length} active order(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);

      ul.innerHTML += `<li>
        <strong>Order ID:</strong> ${data.id || "N/A"}<br>
        <strong>Total:</strong> ৳${data.totalprice || "N/A"}<br>
        <strong>Status:</strong> <span style="color:#e67e22;">${data.status || "N/A"}</span><br>
        <button type="button" class="btn-select" onclick="autoSelectOrder('${data.id || ""}')">Manage Order</button>
      </li>`;
    }
  } catch (err) {
    showMsg("ownerLoadMsg", "❌ " + err.message, "error");
  }
}

async function loadAvailableRiders(event) {
  if (event) event.preventDefault();

  const select = document.getElementById("riderSelect");
  select.innerHTML = `<option value="">Loading riders...</option>`;
  showMsg("ownerMsg", "⏳ Loading available riders...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:getAvailableRiders/>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/owner/orders", body);
    console.log("AVAILABLE RIDERS SOAP:", new XMLSerializer().serializeToString(doc));

    const list = getList(doc, "return");
    select.innerHTML = `<option value="">Select Available Rider</option>`;

    if (list.length === 0) {
      showMsg("ownerMsg", "ℹ️ No available riders found. Check riders.json and rider availability.", "info");
      return;
    }

    let added = 0;

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);

      const riderId = (data.id || "").trim();
      const riderName = (data.name || "").trim();

      if (!riderId) continue;

      const label = riderName ? `${riderName} (${riderId})` : riderId;
      select.innerHTML += `<option value="${riderId}">${label}</option>`;
      added++;
    }

    if (added === 0) {
      select.innerHTML = `<option value="">Select Available Rider</option>`;
      return showMsg("ownerMsg", "❌ Rider response এসেছে, but usable id/name parse করা যায়নি. Check DeliveryRider SOAP serialization.", "error");
    }

    showMsg("ownerMsg", `✅ Loaded ${added} available rider(s).`, "success");
  } catch (err) {
    select.innerHTML = `<option value="">Select Available Rider</option>`;
    showMsg("ownerMsg", "❌ " + err.message, "error");
  }
}

async function updateStatus(event) {
  if (event) event.preventDefault();

  const orderId = document.getElementById("ownerOrderId").value.trim();
  const status  = document.getElementById("ownerStatus").value;

  if (!orderId) {
    return showMsg("ownerMsg", "❌ Enter Order ID", "error");
  }

  showMsg("ownerMsg", "⏳ Updating status...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:updateOrderStatus>
        <orderId>${orderId}</orderId>
        <status>${status}</status>
      </ws:updateOrderStatus>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    await sendSoap(BASE + "/owner/orders", body);
    showMsg("ownerMsg", "✅ Status updated successfully!", "success");
    getOwnerOrders();
  } catch (err) {
    showMsg("ownerMsg", "❌ " + err.message, "error");
  }
}

async function assignRider(event) {
  if (event) event.preventDefault();

  const orderId = document.getElementById("ownerOrderId").value.trim();
  const riderId = document.getElementById("riderSelect").value;

  if (!orderId) {
    return showMsg("ownerMsg", "❌ Enter Order ID", "error");
  }

  if (!riderId) {
    return showMsg("ownerMsg", "❌ Select a rider first", "error");
  }

  showMsg("ownerMsg", "⏳ Assigning rider...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:assignRiderToOrder>
        <orderId>${orderId}</orderId>
        <riderId>${riderId}</riderId>
      </ws:assignRiderToOrder>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/owner/orders", body);
    const returns = getList(doc, "return");
    const msg = returns.length > 0 ? returns[0].textContent.trim() : "✅ Rider assigned successfully!";
    showMsg("ownerMsg", msg, "success");
    loadAvailableRiders();
  } catch (err) {
    showMsg("ownerMsg", "❌ " + err.message, "error");
  }
}

window.autoSelectOrder = function(id) {
  const orderInput = document.getElementById("ownerOrderId");
  if (orderInput) {
    orderInput.value = id;
    orderInput.scrollIntoView({ behavior: "smooth", block: "center" });
  }
  showMsg("ownerMsg", `Order ${id} selected.`, "info");
};