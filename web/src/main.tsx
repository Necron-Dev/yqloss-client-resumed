import React from "react";
import ReactDOM from "react-dom/client";
import {HashRouter} from "react-router-dom";

import "./globals.css";
import {HOST} from "@/api.tsx";
import App from "@/App.tsx";

(async () => {
  window.preload = {}

  try {
    const response = await fetch(`${HOST}state/get/preload`)
    const preload = await response.json() as string[]


    await Promise.all(
      preload.map(async state => {
        try {
          const response = await fetch(`${HOST}state/get/${state}`)
          window.preload[state] = await response.json()
        } catch (error) {
        }
      })
    )
  } catch (error) {
  }

  ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
      <HashRouter>
        <App/>
      </HashRouter>
    </React.StrictMode>,
  );
})()
