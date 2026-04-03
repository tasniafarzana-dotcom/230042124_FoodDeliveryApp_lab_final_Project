// ============================================================
// common.js — Module 0: Shared SOAP Engine & Utilities
// ============================================================

const BASE = "http://localhost:8080";
const NS   = "http://api.fooddelivery.org/";

let currentUser = null;

document.addEventListener("submit", function (e) {
  e.preventDefault();
});

// ------------------------------------------------------------
// Local session helpers
// ------------------------------------------------------------
function saveCurrentUser() {
  if (currentUser) {
    localStorage.setItem("currentUser", JSON.stringify(currentUser));
  } else {
    localStorage.removeItem("currentUser");
  }
}

function loadCurrentUser() {
  const saved = localStorage.getItem("currentUser");
  if (!saved) {
    currentUser = null;
    return null;
  }

  try {
    currentUser = JSON.parse(saved);
    return currentUser;
  } catch (e) {
    currentUser = null;
    localStorage.removeItem("currentUser");
    return null;
  }
}

function saveLastOrderId(orderId) {
  if (orderId && orderId.trim()) {
    localStorage.setItem("lastOrderId", orderId.trim());
  }
}

function loadLastOrderId() {
  return localStorage.getItem("lastOrderId") || "";
}

function clearLastOrderId() {
  localStorage.removeItem("lastOrderId");
}


function saveSelectedRestaurantId(restaurantId) {
  if (restaurantId && restaurantId.trim()) {
    localStorage.setItem("selectedRestaurantId", restaurantId.trim());
  }
}

function loadSelectedRestaurantId() {
  return localStorage.getItem("selectedRestaurantId") || "";
}

function clearSelectedRestaurantId() {
  localStorage.removeItem("selectedRestaurantId");
}

function saveSelectedMenuItemId(menuItemId) {
  if (menuItemId && menuItemId.trim()) {
    localStorage.setItem("selectedMenuItemId", menuItemId.trim());
  }
}

function loadSelectedMenuItemId() {
  return localStorage.getItem("selectedMenuItemId") || "";
}

function clearSelectedMenuItemId() {
  localStorage.removeItem("selectedMenuItemId");
}

// ------------------------------------------------------------
// Core SOAP Engine
// ------------------------------------------------------------
async function sendSoap(url, body) {
  const res = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "text/xml; charset=utf-8",
      "SOAPAction": '""'
    },
    body
  });

  const text = await res.text();
  const doc  = new DOMParser().parseFromString(text, "text/xml");

  const faultNodes = getList(doc, "faultstring");
  if (faultNodes.length > 0) {
    throw new Error(faultNodes[0].textContent.trim());
  }

  if (!res.ok) {
    const fallbackMsg = text && text.trim()
      ? text.trim().slice(0, 300)
      : `HTTP ${res.status}`;
    throw new Error(fallbackMsg);
  }

  return doc;
}

// ------------------------------------------------------------
// XML Helpers
// ------------------------------------------------------------
function getList(docOrNode, tagName) {
  const lowerTag = tagName.toLowerCase();
  const all = docOrNode.getElementsByTagName("*");
  const result = [];

  for (let i = 0; i < all.length; i++) {
    const local = (all[i].localName || "").toLowerCase();
    const full  = (all[i].tagName || "").toLowerCase();

    if (
      local === lowerTag ||
      full === lowerTag ||
      full.endsWith(":" + lowerTag)
    ) {
      result.push(all[i]);
    }
  }

  return result;
}

function extractData(node) {
  const data = {};
  if (!node) return data;

  const elements = node.getElementsByTagName("*");

  for (let i = 0; i < elements.length; i++) {
    const el = elements[i];

    if (el.children.length === 0 && el.textContent) {
      let tag = el.localName || el.tagName;
      if (tag.includes(":")) tag = tag.split(":")[1];
      data[tag.toLowerCase()] = el.textContent.trim();
    }
  }

  return data;
}

function findFirstText(node, tagName) {
  const nodes = getList(node, tagName);
  if (!nodes.length) return "";
  return (nodes[0].textContent || "").trim();
}

// ------------------------------------------------------------
// UI Helpers
// ------------------------------------------------------------
function showMsg(elementId, text, type = "success") {
  const el = document.getElementById(elementId);
  if (!el) return;

  el.textContent = text;
  el.className = `msg ${type}`;
}

function hideMsg(elementId) {
  const el = document.getElementById(elementId);
  if (!el) return;

  el.textContent = "";
  el.className = "msg";
}

function clearMessages(ids = []) {
  ids.forEach(hideMsg);
}

function logout() {
  currentUser = null;
  saveCurrentUser();

  clearLastOrderId();
  clearSelectedRestaurantId();
  clearSelectedMenuItemId();

  setUIForUser();
  showMsg("authMsg", "Logged out successfully.", "success");
}