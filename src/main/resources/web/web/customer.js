// ============================================================
// customer.js — Module 2: Find Restaurants & View Menu
// ============================================================

function renderCustomerSection() {
  document.getElementById("customerSection").innerHTML = `
    <div class="box">
      <h3>1. Find Restaurants</h3>
      <input id="area" placeholder="Enter Area (e.g. Uttara)" />
      <button onclick="getRestaurants()">Search Restaurants</button>
      <div id="restMsg" class="msg"></div>
      <ul id="restaurantsList"></ul>
    </div>

    <div class="box">
      <h3>2. View Menu</h3>
      <input id="restaurantId" placeholder="Restaurant ID (Auto-filled)" />
      <button onclick="getMenu()">Get Menu</button>
      <div id="menuMsg" class="msg"></div>
      <ul id="menuList"></ul>
    </div>
  `;
}

async function getRestaurants() {
  const area = document.getElementById("area").value || "";
  showMsg("restMsg", "⏳ Searching...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:getRestaurantsByArea><area>${area}</area></ws:getRestaurantsByArea>
    </soapenv:Body></soapenv:Envelope>`;
  try {
    const doc  = await sendSoap(BASE + "/restaurants", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("restaurantsList");
    ul.innerHTML = "";

    if (list.length === 0) return showMsg("restMsg", "ℹ️ No restaurants found.", "info");
    showMsg("restMsg", `✅ Found ${list.length} restaurant(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);
      if (data.id && data.name) {
        ul.innerHTML += `<li>
          <strong>${data.name}</strong>
          <span style="color:#7f8c8d;font-size:12px;">(${data.id})</span><br>
          ${data.cuisinetype || ''} | ${data.area || ''}<br>
          <button class="btn-action" onclick="autoSelectRestaurant('${data.id}')">Select & View Menu</button>
        </li>`;
      } else {
        const fields = Object.keys(data).map(k => `${k}: ${data[k]}`).join(', ');
        ul.innerHTML += `<li><span style="color:#e74c3c;">⚠️ Missing 'id' or 'name'</span>
          <div class="diagnostic">{ ${fields} }</div></li>`;
      }
    }
  } catch (err) { showMsg("restMsg", "❌ " + err.message, "error"); }
}

async function getMenu() {
  const restId = document.getElementById("restaurantId").value;
  if (!restId) return showMsg("menuMsg", "❌ Enter a Restaurant ID first", "error");
  showMsg("menuMsg", "⏳ Loading menu...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:getAvailableItems><restaurantId>${restId}</restaurantId></ws:getAvailableItems>
    </soapenv:Body></soapenv:Envelope>`;
  try {
    const doc  = await sendSoap(BASE + "/menu", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("menuList");
    ul.innerHTML = "";

    if (list.length === 0) return showMsg("menuMsg", "ℹ️ No menu items found.", "info");
    showMsg("menuMsg", `✅ Loaded ${list.length} menu item(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);
      if (data.id) {
        ul.innerHTML += `<li>
          <strong>${data.name || 'Unknown'}</strong> — ৳${data.price || '0'}
          <span style="color:#7f8c8d;font-size:12px;">(${data.id})</span><br>
          ${data.description || ''} | ${data.category || ''}<br>
          <button class="btn-select" onclick="autoSelectItem('${data.id}')">Select Item</button>
        </li>`;
      } else {
        ul.innerHTML += `<li><span style="color:#e74c3c;">⚠️ Missing ID!</span>
          <div class="diagnostic">${Object.keys(data).map(k=>`${k}:${data[k]}`).join(', ')}</div></li>`;
      }
    }
  } catch (err) { showMsg("menuMsg", "❌ " + err.message, "error"); }
}

// Auto-fill helpers
window.autoSelectRestaurant = function(id) {
  document.getElementById("restaurantId").value = id;
  document.getElementById("restaurantId").scrollIntoView({ behavior: "smooth", block: "center" });
  getMenu();
}

window.autoSelectItem = function(id) {
  document.getElementById("menuItemId").value = id;
  document.getElementById("qty").focus();
  document.getElementById("qty").scrollIntoView({ behavior: "smooth", block: "center" });
  showMsg("orderResult", "Item selected! Set quantity and click 'Place Order'.", "info");
}
