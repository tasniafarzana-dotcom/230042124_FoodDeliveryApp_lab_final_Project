// ============================================================
// auth.js — Module 1: Login & Register
// ============================================================

function renderAuthSection() {
  document.getElementById("authSection").innerHTML = `
    <div class="box">
      <h3>Login / Register</h3>
      <input id="authName"  placeholder="Full Name (for register only)" />
      <input id="authEmail" placeholder="Email Address *" type="email" />
      <input id="authPhone" placeholder="Phone Number (for register only)" />
      <input id="authPass"  placeholder="Password *" type="password" />
      <select id="authRole">
        <option value="CUSTOMER">Customer</option>
        <option value="RESTAURANT_OWNER">Restaurant Owner</option>
        <option value="DELIVERY_RIDER">Delivery Rider</option>
      </select>
      <button onclick="register()">Register</button>
      <button onclick="login()" style="background:#2ecc71;">Login</button>
      <div id="authMsg" class="msg"></div>
    </div>
  `;
}

async function register() {
  const name  = document.getElementById("authName").value.trim();
  const email = document.getElementById("authEmail").value.trim();
  const pass  = document.getElementById("authPass").value;
  const phone = document.getElementById("authPhone").value.trim();
  const role  = document.getElementById("authRole").value;

  if (!email || !pass || !name)
    return showMsg("authMsg", "❌ Name, Email, and Password are required!", "error");
  showMsg("authMsg", "⏳ Registering...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:register>
      <name>${name}</name><email>${email}</email>
      <phone>${phone}</phone><password>${pass}</password><role>${role}</role>
    </ws:register></soapenv:Body></soapenv:Envelope>`;
  try {
    await sendSoap(BASE + "/auth", body);
    showMsg("authMsg", "✅ Registration successful! Please click Login.", "success");
  } catch (err) { showMsg("authMsg", "❌ " + err.message, "error"); }
}

async function login() {
  const email = document.getElementById("authEmail").value.trim();
  const pass  = document.getElementById("authPass").value;
  if (!email || !pass)
    return showMsg("authMsg", "❌ Email and Password required!", "error");
  showMsg("authMsg", "⏳ Logging in...", "info");

  const body = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="${NS}">
    <soapenv:Body><ws:login>
      <email>${email}</email><password>${pass}</password>
    </ws:login></soapenv:Body></soapenv:Envelope>`;
  try {
    const doc  = await sendSoap(BASE + "/auth", body);
    const returns = getList(doc, "return");
    const node = returns.length > 0 ? returns[0] : doc.documentElement;
    const data = extractData(node);

    currentUser = {
      id:    data.id    || email,
      name:  data.name  || email.split("@")[0],
      email: email,
      role: (data.role  || document.getElementById("authRole").value || "CUSTOMER").toUpperCase()
    };
    setUIForUser();
  } catch (err) { showMsg("authMsg", "❌ Login failed: " + err.message, "error"); }
}
