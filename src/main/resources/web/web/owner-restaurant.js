// ============================================================
// owner-restaurant.js —  Add Restaurant & My Restaurants
// ============================================================

function renderOwnerRestaurantSection() {
  document.getElementById("ownerSection").innerHTML = `
    <div class="box">
      <h3>🏪 Add New Restaurant</h3>
      <input id="newRestName" placeholder="Restaurant Name *" />
      <input id="newRestCuisine" placeholder="Cuisine Type *" />
      <input id="newRestPhone" placeholder="Restaurant Phone *" />
      <input id="newRestStreet" placeholder="Street Address *" />
      <input id="newRestArea" placeholder="Area *" />
      <input id="newRestCity" placeholder="City *" />
      <button type="button" onclick="addRestaurant(event)" style="background:#27ae60;">➕ Add Restaurant</button>
      <div id="addRestMsg" class="msg"></div>
    </div>

    <div class="box">
      <h3>📋 My Restaurants</h3>
      <button type="button" onclick="loadMyRestaurants(event)">Load My Restaurants</button>
      <div id="myRestMsg" class="msg"></div>
      <ul id="myRestaurantsList"></ul>
    </div>
  `;
}

async function addRestaurant(event) {
  if (event) event.preventDefault();

  if (!currentUser || !currentUser.id) {
    return showMsg("addRestMsg", "❌ Please login first.", "error");
  }

  const name    = document.getElementById("newRestName").value.trim();
  const cuisine = document.getElementById("newRestCuisine").value.trim();
  const phone   = document.getElementById("newRestPhone").value.trim();
  const street  = document.getElementById("newRestStreet").value.trim();
  const area    = document.getElementById("newRestArea").value.trim();
  const city    = document.getElementById("newRestCity").value.trim();

  if (!name || !cuisine || !phone || !street || !area || !city) {
    return showMsg("addRestMsg", "❌ All fields are required!", "error");
  }

  showMsg("addRestMsg", "⏳ Adding restaurant...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:registerRestaurant>
        <ownerId>${currentUser.id}</ownerId>
        <n>${name}</n>
        <cuisineType>${cuisine}</cuisineType>
        <phone>${phone}</phone>
        <street>${street}</street>
        <area>${area}</area>
        <city>${city}</city>
        <latitude>23.8103</latitude>
        <longitude>90.4125</longitude>
      </ws:registerRestaurant>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/restaurants", body);
    const returns = getList(doc, "return");
    const node = returns.length > 0 ? returns[0] : doc.documentElement;

    const idNodes = getList(node, "id");
    const restIdNode = idNodes.find(n => (n.textContent || "").trim().startsWith("RST-"));
    const restId = restIdNode ? restIdNode.textContent.trim() : "";

    if (restId) {
      saveSelectedRestaurantId(restId);

      const menuRestIdEl = document.getElementById("menuRestId");
      const ownerRestaurantIdEl = document.getElementById("ownerRestaurantId");

      if (menuRestIdEl) menuRestIdEl.value = restId;
      if (ownerRestaurantIdEl) ownerRestaurantIdEl.value = restId;

      showMsg("addRestMsg", `✅ Restaurant added! ID: ${restId}`, "success");
    } else {
      showMsg("addRestMsg", "✅ Restaurant added!", "success");
    }

    document.getElementById("newRestName").value = "";
    document.getElementById("newRestCuisine").value = "";
    document.getElementById("newRestPhone").value = "";
    document.getElementById("newRestStreet").value = "";
    document.getElementById("newRestArea").value = "";
    document.getElementById("newRestCity").value = "";

    loadMyRestaurants();
  } catch (err) {
    showMsg("addRestMsg", "❌ " + err.message, "error");
  }
}

async function loadMyRestaurants(event) {
  if (event) event.preventDefault();

  if (!currentUser || !currentUser.id) {
    return showMsg("myRestMsg", "❌ Please login first.", "error");
  }

  showMsg("myRestMsg", "⏳ Loading your restaurants...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:getOwnerRestaurants>
        <ownerId>${currentUser.id}</ownerId>
      </ws:getOwnerRestaurants>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/restaurants", body);
    const list = getList(doc, "return");
    const ul = document.getElementById("myRestaurantsList");
    ul.innerHTML = "";

    if (list.length === 0) {
      return showMsg("myRestMsg", "ℹ️ No restaurants found.", "info");
    }

    showMsg("myRestMsg", `✅ Found ${list.length} restaurant(s).`, "success");

    for (let i = 0; i < list.length; i++) {
      const data = extractData(list[i]);

      if (data.id) {
        ul.innerHTML += `<li>
          <strong>${data.name || "Unknown"}</strong>
          <span style="color:#7f8c8d;font-size:12px;">(${data.id})</span><br>
          ${data.cuisinetype || ""} | ${data.area || ""} | ${data.city || ""}<br>
          <button type="button" class="btn-action" onclick="useRestaurantForMenu('${data.id}')">Use for Menu</button>
          <button type="button" class="btn-select" onclick="useRestaurantForOrders('${data.id}')">Use for Orders</button>
        </li>`;
      } else {
        ul.innerHTML += `<li>
          <span style="color:#e74c3c;">⚠️ Missing ID!</span>
          <div class="diagnostic">${Object.keys(data).map(k => `${k}:${data[k]}`).join(", ")}</div>
        </li>`;
      }
    }
  } catch (err) {
    showMsg("myRestMsg", "❌ " + err.message, "error");
  }
}

window.useRestaurantForMenu = function(id) {
  saveSelectedRestaurantId(id);

  const menuRestIdEl = document.getElementById("menuRestId");
  if (menuRestIdEl) {
    menuRestIdEl.value = id;
    menuRestIdEl.scrollIntoView({ behavior: "smooth", block: "center" });
  }

  showMsg("addMenuMsg", "Restaurant selected for menu management.", "info");
};

window.useRestaurantForOrders = function(id) {
  saveSelectedRestaurantId(id);

  const ownerRestaurantIdEl = document.getElementById("ownerRestaurantId");
  if (ownerRestaurantIdEl) {
    ownerRestaurantIdEl.value = id;
    ownerRestaurantIdEl.scrollIntoView({ behavior: "smooth", block: "center" });
  }

  showMsg("ownerLoadMsg", "Restaurant selected for order management.", "info");
};