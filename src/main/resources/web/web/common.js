// ============================================================
// common.js — Module 0: Shared SOAP Engine & Utilities
// ============================================================

const BASE = "http://localhost:8080";
const NS   = "http://api.fooddelivery.org/";

let currentUser = null;

// --- Core SOAP Engine ---
async function sendSoap(url, body) {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "text/xml; charset=utf-8", "SOAPAction": '""' },
    body: body
  });
  const text = await res.text();
  const doc  = new DOMParser().parseFromString(text, "text/xml");
  const faultNodes = getList(doc, "faultstring");
  if (faultNodes.length > 0) throw new Error(faultNodes[0].textContent);
  return doc;
}

function getList(doc, tagName) {
  const lowerTag = tagName.toLowerCase();
  const all = doc.getElementsByTagName("*");
  const res = [];
  for (let i = 0; i < all.length; i++) {
    const local = (all[i].localName || "").toLowerCase();
    const full  = (all[i].tagName  || "").toLowerCase();
    if (local === lowerTag || full === lowerTag || full.includes(":" + lowerTag)) {
      res.push(all[i]);
    }
  }
  return res;
}

function extractData(node) {
  const data = {};
  if (!node) return data;
  const elements = node.getElementsByTagName("*");
  for (let i = 0; i < elements.length; i++) {
    const el = elements[i];
    if (el.children.length === 0 && el.textContent) {
      let tag = el.localName || el.tagName;
      if (tag.includes(':')) tag = tag.split(':')[1];
      data[tag.toLowerCase()] = el.textContent.trim();
    }
  }
  return data;
}

function showMsg(elementId, text, type = "success") {
  const el = document.getElementById(elementId);
  if (!el) return;
  el.textContent = text;
  el.className = `msg ${type}`;
}

function hideMsg(elementId) {
  const el = document.getElementById(elementId);
  if (el) el.className = "msg";
}

function logout() {
  currentUser = null;
  setUIForUser();
  showMsg("authMsg", "Logged out successfully.", "success");
}
