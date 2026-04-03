// ============================================================
// owner-restaurant.js — Module 4: Add Restaurant & My Restaurants
// ============================================================

function renderOwnerRestaurantSection() {
  document.getElementById("ownerSection").innerHTML = `
    <div class="box">
      <h3>🏪 Add New Restaurant</h3>
      <input id="newRestName"    placeholder="Restaurant Name *" />
      <input id="newRestCuisine" placeholder="Cuisine Type (e.g. Bangladeshi, Chinese) *" />
      <input id="newRestPhone"   placeholder="Restaurant Phone *" />
      <input id="newRestStreet"  placeholder="Street Address *" />
      <input id="newRestArea"    placeholder="Area (e.g. Uttara) *" />
      <input id="newRestCity"    placeholder="City (e.g. Dhaka) *" />
      <button onclick="addRestaurant()" style="background:#27ae60;">➕ Add Restaurant</button>
      <div id="addRestMsg" class="msg"></div>
    </div>

    <div class="box">
      <h3>📋 My Restaurants</h3>
      <button onclick="loadMyRestaurants()">🔄 Load My Restaurants</button>
      <div id="myRestMsg" class="msg"></div>
      <ul id="myRestaurantsList"></ul>
    </div>
  `;
}

async function addRestaurant() {
  const name    = document.getElementById("newRestName").value.trim();
  const cuisine = document.getElementById("newRestCuisine").value.trim();
  const phone   = document.getElementById("newRestPhone").value.trim();
  const street  = document.getElementById("newRestStreet").value.trim();
  const area    = document.getElementById("newRestArea").value.trim();
  const city    = document.getElementById("newRestCity").value.trim();

  if (!name || !cuisine || !phone || !street || !area || !city)
    return showMsg("addRestMsg", "❌ All fields are required!", "error");
  showMsg("addRestMsg", "⏳ Adding restaurant...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:registerRestaurant>
      <ownerId>${currentUser.id}</ownerId>
      <n>${name}</n><cuisineType>${cuisine}</cuisineType>
      <phone>${phone}</phone><street>${street}</street>
      <area>${area}</area><city>${city}</city>
      <latitude>23.8103</latitude><longitude>90.4125</longitude>
    </ws:registerRestaurant></soapenv:Body></soapenv:Envelope>`;
  try {
    const doc     = await sendSoap(BASE + "/restaurants", body);
    const returns = getList(doc, "return");
    const data    = extractData(returns.length > 0 ? returns[0] : doc.documentElement);

    const restId = data.id || "N/A";
    showMsg("addRestMsg", `✅ Restaurant added! ID: ${restId}`, "success");

    if (data.id) {
      document.getElementById("menuRestId") && (document.getElementById("menuRestId").value = data.id);
      document.getElementById("ownerRestaurantId") && (document.getElementById("ownerRestaurantId").value = data.id);
    }

    ["newRestName","newRestCuisine","newRestPhone","newRestStreet","newRestArea","newRestCity"]
      .forEach(id => document.getElementById(id).value = "");

    loadMyRestaurants();
  } catch (err) { showMsg("addRestMsg", "❌ " + err.message, "error"); }
}

async function loadMyRestaurants() {
  showMsg("myRestMsg", "⏳ Loading your restaurants...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:getOwnerRestaurants>
      <ownerId>${currentUser.id}</ownerId>
    </ws:getOwnerRestaurants></soapenv:Body></soapenv:Envelope>`;
  try {
    const doc  = await sendSoap(BASE + "/restaurants", body);
    const list = getList(doc, "return");
    const ul   = document.getElementById("myRestaurantsList");
    ul.innerHTML = "";

    if (list.length === 0) return showMsg("myRestMsg", "ℹ️ No restaurants yet. Add one above!", "info");
    showMsg("myRestMsg", `✅ You have ${list.length} restaurant(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);
      if (data.id) {
        ul.innerHTML += `<li>
          <strong>${data.name || 'Unknown'}</strong>
          <span style="color:#7f8c8d;font-size:12px;"> (${data.id})</span><br>
          ${data.cuisinetype || ''} | ${data.area || ''} |
          <span style="color:${data.open==='true'?'#27ae60':'#e74c3c'}">
            ${data.open === 'true' ? '🟢 OPEN' : '🔴 CLOSED'}
          </span><br>
          <button class="btn-action" onclick="autoFillMenuRestId('${data.id}')">➕ Add Menu Item</button>
          <button class="btn-select" onclick="loadOrdersForRestaurant('${data.id}')">📦 View Orders</button>
        </li>`;
      }
    }
  } catch (err) { showMsg("myRestMsg", "❌ " + err.message, "error"); }
}

window.autoFillMenuRestId = function(id) {
  document.getElementById("menuRestId") && (document.getElementById("menuRestId").value = id);
  document.getElementById("ownerRestaurantId") && (document.getElementById("ownerRestaurantId").value = id);
  showMsg("addMenuMsg", "Restaurant selected! Now add menu items.", "info");
  document.getElementById("menuRestId").scrollIntoView({ behavior: "smooth", block: "center" });
}

window.loadOrdersForRestaurant = function(id) {
  document.getElementById("ownerRestaurantId").value = id;
  getOwnerOrders();
  document.getElementById("ownerRestaurantId").scrollIntoView({ behavior: "smooth", block: "center" });
}
