// ============================================================
// order.js — Place Order, Pay & Track
// ============================================================

function renderOrderSection() {
  document.getElementById("customerSection").innerHTML += `
    <div class="box">
      <h3>3. Place Order</h3>
      <input id="menuItemId" placeholder="Menu Item ID (Auto-filled)" />
      <input id="qty" placeholder="Quantity" type="number" min="1" value="1" />
      <button type="button" id="placeOrderBtn" onclick="placeOrder(event)">Place Order</button>
      <div id="orderResult" class="msg"></div>
    </div>

    <div class="box">
      <h3>4. Pay & Track</h3>
      <input id="orderIdActions" placeholder="Order ID (Auto-filled)" />
      <select id="payMethod">
        <option value="CASH">CASH</option>
        <option value="CARD">CARD</option>
        <option value="MOBILE_BANKING">MOBILE_BANKING</option>
      </select>
      <button type="button" id="payNowBtn" onclick="payOrder(event)">Pay Now</button>
      <button type="button" onclick="trackOrder(event)" style="background:#f39c12;">Track Order</button>
      <div id="actionResult" class="msg"></div>
    </div>
  `;
}

async function placeOrder(event) {
  if (event) event.preventDefault();

  const itemId = document.getElementById("menuItemId").value.trim();
  const restId = document.getElementById("restaurantId").value.trim();
  const qtyRaw = document.getElementById("qty").value;
  const qty = parseInt(qtyRaw || "1", 10);

  const placeBtn = document.getElementById("placeOrderBtn");
  const payBtn   = document.getElementById("payNowBtn");

  if (!currentUser || !currentUser.id) {
    return showMsg("orderResult", "❌ Please login first.", "error");
  }

  if (!itemId || !restId) {
    return showMsg("orderResult", "❌ Please provide Restaurant ID and Menu Item ID", "error");
  }

  if (Number.isNaN(qty) || qty <= 0) {
    return showMsg("orderResult", "❌ Quantity must be a valid positive number", "error");
  }

  document.getElementById("orderIdActions").value = "";
  clearLastOrderId();

  saveSelectedRestaurantId(restId);
  saveSelectedMenuItemId(itemId);

  if (placeBtn) placeBtn.disabled = true;
  if (payBtn) payBtn.disabled = true;

  showMsg("orderResult", "⏳ Placing order...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:placeOrder>
        <userId>${currentUser.id}</userId>
        <restaurantId>${restId}</restaurantId>
        <menuItemId>${itemId}</menuItemId>
        <quantity>${qty}</quantity>
        <deliveryAddressId>DEFAULT</deliveryAddressId>
      </ws:placeOrder>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/customer/orders", body);
    const returns = getList(doc, "return");

    if (returns.length === 0) {
      throw new Error("No order returned from server");
    }

    const idNodes = getList(returns[0], "id");
    const orderIdNode = idNodes.find(n => (n.textContent || "").trim().startsWith("ORD-"));

    if (!orderIdNode) {
      throw new Error("Valid order ID not found in response");
    }

    const orderId = orderIdNode.textContent.trim();

    showMsg("orderResult", `✅ Order Placed! ID: ${orderId}`, "success");
    document.getElementById("orderIdActions").value = orderId;
    saveLastOrderId(orderId);

    if (payBtn) payBtn.disabled = false;
  } catch (err) {
    showMsg("orderResult", "❌ " + err.message, "error");
  } finally {
    if (placeBtn) placeBtn.disabled = false;
  }
}

async function payOrder(event) {
  if (event) event.preventDefault();

  const orderId = document.getElementById("orderIdActions").value.trim();
  const method = document.getElementById("payMethod").value;

  if (!orderId) {
    return showMsg("actionResult", "❌ Enter Order ID", "error");
  }

  showMsg("actionResult", "⏳ Processing payment...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:payOrder>
        <orderId>${orderId}</orderId>
        <method>${method}</method>
        <amount>0</amount>
      </ws:payOrder>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    await sendSoap(BASE + "/payments", body);
    saveLastOrderId(orderId);
    showMsg("actionResult", "✅ Payment Processed Successfully!", "success");
  } catch (err) {
    showMsg("actionResult", "❌ " + err.message, "error");
  }
}

async function trackOrder(event) {
  if (event) event.preventDefault();

  const orderId = document.getElementById("orderIdActions").value.trim();

  if (!orderId) {
    return showMsg("actionResult", "❌ Enter Order ID", "error");
  }

  showMsg("actionResult", "⏳ Checking status...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:getOrderStatus>
        <orderId>${orderId}</orderId>
      </ws:getOrderStatus>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/order/status", body);
    const returns = getList(doc, "return");
    const status = returns.length > 0
      ? returns[0].textContent.trim()
      : doc.documentElement.textContent.trim();

    saveLastOrderId(orderId);
    showMsg("actionResult", `📍 Current Status: ${status}`, "success");
  } catch (err) {
    showMsg("actionResult", "❌ " + err.message, "error");
  }
}