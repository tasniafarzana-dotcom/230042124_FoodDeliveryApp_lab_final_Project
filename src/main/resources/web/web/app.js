// ============================================================
// app.js — Main App Controller (UI routing)
// ============================================================

function setUIForUser() {
  const allMsgs = ["authMsg","restMsg","menuMsg","orderResult","actionResult",
                   "ownerLoadMsg","ownerMsg","addRestMsg","addMenuMsg","myRestMsg",
                   "pendingTaskMsg","activeDeliveryMsg"];
  allMsgs.forEach(hideMsg);

  if (!currentUser) {
    document.getElementById("authSection").classList.remove("hidden");
    document.getElementById("userHeader").classList.add("hidden");
    document.getElementById("customerSection").classList.add("hidden");
    document.getElementById("ownerSection").classList.add("hidden");
    document.getElementById("riderSection").classList.add("hidden");
    return;
  }

  document.getElementById("authSection").classList.add("hidden");
  document.getElementById("userHeader").classList.remove("hidden");
  document.getElementById("welcomeText").innerText = `Hello, ${currentUser.name}`;

  const badge = document.getElementById("userIdBadge");
  badge.innerText = `${currentUser.role} | ${currentUser.id}`;
  badge.className = "badge" + (currentUser.role === "DELIVERY_RIDER" ? " badge-rider" : "");

  document.getElementById("customerSection").classList.add("hidden");
  document.getElementById("ownerSection").classList.add("hidden");
  document.getElementById("riderSection").classList.add("hidden");

  if (currentUser.role === "CUSTOMER") {
    document.getElementById("customerSection").classList.remove("hidden");
  } else if (currentUser.role === "RESTAURANT_OWNER") {
    document.getElementById("ownerSection").classList.remove("hidden");
  } else if (currentUser.role === "DELIVERY_RIDER") {
    document.getElementById("riderSection").classList.remove("hidden");
    loadPendingTasks();
    loadActiveDeliveries();
  }
}

// Render all sections on page load
window.addEventListener("DOMContentLoaded", () => {
  renderAuthSection();          // Module 1
  renderCustomerSection();      // Module 2
  renderOrderSection();         // Module 3
  renderOwnerRestaurantSection(); // Module 4
  renderOwnerMenuSection();     // Module 5
  renderOwnerOrdersSection();   // Module 6
  renderRiderSection();         // Module 7
});
