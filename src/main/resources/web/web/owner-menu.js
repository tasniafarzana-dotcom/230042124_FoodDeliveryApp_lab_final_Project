// ============================================================
// owner-menu.js — Module 5: Add Menu Items
// ============================================================

function renderOwnerMenuSection() {
  document.getElementById("ownerSection").innerHTML += `
    <div class="box">
      <h3>🍽️ Add Menu Item</h3>
      <input id="menuRestId"      placeholder="Restaurant ID *" />
      <input id="newItemName"     placeholder="Item Name *" />
      <input id="newItemDesc"     placeholder="Description" />
      <input id="newItemPrice"    placeholder="Price (TK) *" type="number" min="0" step="0.01" />
      <input id="newItemCategory" placeholder="Category (e.g. Main Course, Drinks) *" />
      <input id="newItemQty"      placeholder="Stock Quantity *" type="number" min="0" value="10" />
      <button onclick="addMenuItem()" style="background:#27ae60;">➕ Add Menu Item</button>
      <div id="addMenuMsg" class="msg"></div>
    </div>
  `;
}

async function addMenuItem() {
  const restId   = document.getElementById("menuRestId").value.trim();
  const name     = document.getElementById("newItemName").value.trim();
  const desc     = document.getElementById("newItemDesc").value.trim();
  const price    = document.getElementById("newItemPrice").value.trim();
  const category = document.getElementById("newItemCategory").value.trim();
  const qty      = document.getElementById("newItemQty").value.trim() || "10";

  if (!restId || !name || !price || !category)
    return showMsg("addMenuMsg", "❌ Restaurant ID, Name, Price, and Category are required!", "error");
  if (isNaN(price) || parseFloat(price) <= 0)
    return showMsg("addMenuMsg", "❌ Price must be a valid positive number!", "error");
  showMsg("addMenuMsg", "⏳ Adding menu item...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:addMenuItem>
      <restaurantId>${restId}</restaurantId>
      <n>${name}</n><description>${desc}</description>
      <price>${price}</price><category>${category}</category>
      <quantity>${qty}</quantity>
    </ws:addMenuItem></soapenv:Body></soapenv:Envelope>`;
  try {
    await sendSoap(BASE + "/menu", body);
    showMsg("addMenuMsg", `✅ "${name}" added! Price: ৳${price}, Stock: ${qty}`, "success");
    ["newItemName","newItemDesc","newItemPrice","newItemCategory"]
      .forEach(id => document.getElementById(id).value = "");
    document.getElementById("newItemQty").value = "10";
  } catch (err) { showMsg("addMenuMsg", "❌ " + err.message, "error"); }
}
