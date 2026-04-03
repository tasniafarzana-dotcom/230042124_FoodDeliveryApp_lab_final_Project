// ============================================================
// owner-menu.js —  Add Menu Items
// ============================================================

function renderOwnerMenuSection() {
  document.getElementById("ownerSection").innerHTML += `
    <div class="box">
      <h3>🍽️ Add Menu Item</h3>
      <input id="menuRestId" placeholder="Restaurant ID *" />
      <input id="newItemName" placeholder="Item Name *" />
      <input id="newItemDesc" placeholder="Description" />
      <input id="newItemPrice" placeholder="Price (TK) *" type="number" min="0" step="0.01" />
      <input id="newItemCategory" placeholder="Category *" />
      <input id="newItemQty" placeholder="Stock Quantity *" type="number" min="0" value="10" />
      <button type="button" onclick="addMenuItem(event)" style="background:#27ae60;">➕ Add Menu Item</button>
      <div id="addMenuMsg" class="msg"></div>
    </div>
  `;
}

async function addMenuItem(event) {
  if (event) event.preventDefault();

  const restId   = document.getElementById("menuRestId").value.trim();
  const name     = document.getElementById("newItemName").value.trim();
  const desc     = document.getElementById("newItemDesc").value.trim();
  const priceRaw = document.getElementById("newItemPrice").value.trim();
  const category = document.getElementById("newItemCategory").value.trim();
  const qtyRaw   = document.getElementById("newItemQty").value.trim() || "10";

  const price = parseFloat(priceRaw);
  const qty   = parseInt(qtyRaw, 10);

  if (!restId || !name || !priceRaw || !category) {
    return showMsg("addMenuMsg", "❌ Restaurant ID, Name, Price, and Category are required!", "error");
  }

  if (Number.isNaN(price) || price <= 0) {
    return showMsg("addMenuMsg", "❌ Price must be a valid positive number!", "error");
  }

  if (Number.isNaN(qty) || qty < 0) {
    return showMsg("addMenuMsg", "❌ Quantity must be zero or a positive number!", "error");
  }

  saveSelectedRestaurantId(restId);
  showMsg("addMenuMsg", "⏳ Adding menu item...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body>
      <ws:addMenuItem>
        <restaurantId>${restId}</restaurantId>
        <n>${name}</n>
        <description>${desc}</description>
        <price>${price}</price>
        <category>${category}</category>
        <quantity>${qty}</quantity>
      </ws:addMenuItem>
    </soapenv:Body>
  </soapenv:Envelope>`;

  try {
    const doc = await sendSoap(BASE + "/menu", body);
    const returns = getList(doc, "return");
    const node = returns.length > 0 ? returns[0] : doc.documentElement;

    const idNodes = getList(node, "id");
    const itemIdNode = idNodes.find(n => (n.textContent || "").trim().startsWith("ITM-"));
    const itemId = itemIdNode ? itemIdNode.textContent.trim() : "";

    if (itemId) {
      showMsg("addMenuMsg", `✅ "${name}" added successfully! Item ID: ${itemId}`, "success");
    } else {
      showMsg("addMenuMsg", `✅ "${name}" added successfully!`, "success");
    }

    document.getElementById("newItemName").value = "";
    document.getElementById("newItemDesc").value = "";
    document.getElementById("newItemPrice").value = "";
    document.getElementById("newItemCategory").value = "";
    document.getElementById("newItemQty").value = "10";
  } catch (err) {
    showMsg("addMenuMsg", "❌ " + err.message, "error");
  }
}