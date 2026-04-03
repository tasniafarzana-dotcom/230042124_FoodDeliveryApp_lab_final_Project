// ============================================================
// app.js — Main App Controller (UI routing)
// ============================================================

function setUIForUser() {
  const allMsgs = [
    "authMsg",
    "restMsg",
    "menuMsg",
    "orderResult",
    "actionResult",
    "ownerLoadMsg",
    "ownerMsg",
    "addRestMsg",
    "addMenuMsg",
    "myRestMsg",
    "pendingTaskMsg",
    "activeDeliveryMsg"
  ];
  clearMessages(allMsgs);

  const authSection     = document.getElementById("authSection");
  const userHeader      = document.getElementById("userHeader");
  const customerSection = document.getElementById("customerSection");
  const ownerSection    = document.getElementById("ownerSection");
  const riderSection    = document.getElementById("riderSection");

  if (!currentUser) {
    authSection.classList.remove("hidden");
    userHeader.classList.add("hidden");
    customerSection.classList.add("hidden");
    ownerSection.classList.add("hidden");
    riderSection.classList.add("hidden");
    return;
  }

  authSection.classList.add("hidden");
  userHeader.classList.remove("hidden");

  document.getElementById("welcomeText").innerText = `Hello, ${currentUser.name}`;

  const badge = document.getElementById("userIdBadge");
  badge.innerText = `${currentUser.role} | ${currentUser.id}`;
  badge.className = "badge" + (currentUser.role === "DELIVERY_RIDER" ? " badge-rider" : "");

  customerSection.classList.add("hidden");
  ownerSection.classList.add("hidden");
  riderSection.classList.add("hidden");

  if (currentUser.role === "CUSTOMER") {
    customerSection.classList.remove("hidden");
    restoreCustomerState();
  } else if (currentUser.role === "RESTAURANT_OWNER") {
    ownerSection.classList.remove("hidden");
    restoreOwnerState();
  } else if (currentUser.role === "DELIVERY_RIDER") {
    riderSection.classList.remove("hidden");
    loadPendingTasks();
    loadActiveDeliveries();
  }
}

// ------------------------------------------------------------
// State restoration helpers
// ------------------------------------------------------------
function restoreCustomerState() {
  const savedRestaurantId = loadSelectedRestaurantId();
  const savedMenuItemId   = loadSelectedMenuItemId();
  const savedOrderId      = loadLastOrderId();

  const restaurantInput = document.getElementById("restaurantId");
  const menuItemInput   = document.getElementById("menuItemId");
  const orderIdInput    = document.getElementById("orderIdActions");

  if (restaurantInput && savedRestaurantId) {
    restaurantInput.value = savedRestaurantId;
  }

  if (menuItemInput && savedMenuItemId) {
    menuItemInput.value = savedMenuItemId;
  }

  if (orderIdInput && savedOrderId) {
    orderIdInput.value = savedOrderId;
  }
}

function restoreOwnerState() {
  const savedRestaurantId = loadSelectedRestaurantId();

  const ownerRestaurantInput = document.getElementById("ownerRestaurantId");
  const menuRestaurantInput  = document.getElementById("menuRestId");

  if (ownerRestaurantInput && savedRestaurantId) {
    ownerRestaurantInput.value = savedRestaurantId;
  }

  if (menuRestaurantInput && savedRestaurantId) {
    menuRestaurantInput.value = savedRestaurantId;
  }
}

// ------------------------------------------------------------
// App bootstrap
// ------------------------------------------------------------
window.addEventListener("DOMContentLoaded", () => {
  renderAuthSection();
  renderCustomerSection();
  renderOrderSection();
  renderOwnerRestaurantSection();
  renderOwnerMenuSection();
  renderOwnerOrdersSection();
  renderRiderSection();

  loadCurrentUser();
  setUIForUser();
});