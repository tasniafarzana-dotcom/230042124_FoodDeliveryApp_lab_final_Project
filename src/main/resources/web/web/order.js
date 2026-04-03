// ============================================================
// order.js — Module 3: Place Order, Pay & Track
// ============================================================

function renderOrderSection() {
  document.getElementById("customerSection").innerHTML +=  `
    <div class="box">
      <h3>3. Place Order</h3>
      <input id="menuItemId" placeholder="Menu Item ID (Auto-filled)" />
      <input id="qty" placeholder="Quantity" type="number" min="1" value="1" />
      <input id="coupon" placeholder="Coupon Code (Optional)" />
      <button onclick="placeOrder()">Place Order</button>
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
      <button onclick="payOrder()">Pay Now</button>
      <button onclick="trackOrder()" style="background:#f39c12;">Track Order</button>
      <div id="actionResult" class="msg"></div>
    </div>
  `;
}

async function placeOrder() {
  const itemId = document.getElementById("menuItemId").value;
  const restId = document.getElementById("restaurantId").value;
  const qty    = document.getElementById("qty").value || 1;

  if (!itemId || !restId)
    return showMsg("orderResult", "❌ Please provide Restaurant ID and Menu Item ID", "error");
  showMsg("orderResult", "⏳ Placing order...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:placeOrder>
      <userId>${currentUser.id}</userId><restaurantId>${restId}</restaurantId>
      <menuItemId>${itemId}</menuItemId><quantity>${qty}</quantity>
      <couponCode>${document.getElementById("coupon").value}</couponCode>
      <deliveryAddressId>DEFAULT</deliveryAddressId>
    </ws:placeOrder></soapenv:Body></soapenv:Envelope>`;
  try {
    const doc     = await sendSoap(BASE + "/customer/orders", body);
    const returns = getList(doc, "return");
    const data    = extractData(returns.length > 0 ? returns[0] : doc.documentElement);

    if (data.id) {
      showMsg("orderResult", `✅ Order Placed! ID: ${data.id}`, "success");
      document.getElementById("orderIdActions").value = data.id;
    } else {
      showMsg("orderResult", "⚠️ Order processed but no ID returned.", "info");
    }
  } catch (err) { showMsg("orderResult", "❌ " + err.message, "error"); }
}

async function payOrder() {
  const orderId = document.getElementById("orderIdActions").value;
  if (!orderId) return showMsg("actionResult", "❌ Enter Order ID", "error");
  showMsg("actionResult", "⏳ Processing payment...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:payOrder>
      <orderId>${orderId}</orderId>
      <method>${document.getElementById("payMethod").value}</method><amount>0</amount>
    </ws:payOrder></soapenv:Body></soapenv:Envelope>`;
  try {
    await sendSoap(BASE + "/payments", body);
    showMsg("actionResult", "✅ Payment Processed Successfully!", "success");
  } catch (err) { showMsg("actionResult", "❌ " + err.message, "error"); }
}

async function trackOrder() {
  const orderId = document.getElementById("orderIdActions").value;
  if (!orderId) return showMsg("actionResult", "❌ Enter Order ID", "error");
  showMsg("actionResult", "⏳ Checking status...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:getOrderStatus><orderId>${orderId}</orderId></ws:getOrderStatus>
    </soapenv:Body></soapenv:Envelope>`;
  try {
    const doc     = await sendSoap(BASE + "/order/status", body);
    const returns = getList(doc, "return");
    const status  = returns.length > 0 ? returns[0].textContent : doc.documentElement.textContent;
    showMsg("actionResult", `📍 Current Status: ${status}`, "success");
  } catch (err) { showMsg("actionResult", "❌ " + err.message, "error"); }
}
