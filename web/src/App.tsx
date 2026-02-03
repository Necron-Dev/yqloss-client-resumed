import {Route, Routes, useSearchParams} from "react-router-dom";
import HudPage from "@/pages/hud/HudPage.tsx";
import ConfigScreen from "@/pages/config/ConfigScreen.tsx";
import {useYcrState} from "@/api.tsx";
import {useEffect} from "react";
import LeapMenuScreen from "@/pages/leap-menu/LeapMenuScreen.tsx";

export default function App() {
  const [params] = useSearchParams()

  const [uuid] = useYcrState<string>("uuid")

  window.uuid = uuid

  const [token, setToken] = useYcrState<string>("token")

  useEffect(() => {
    setTimeout(() => {
      setToken(params.get("token") ?? "")
    }, 0)
  }, [window.location.href])

  const [href] = useYcrState<string>("href")

  useEffect(() => {
    if (href !== undefined && window.location.href !== href) {
      window.location.href = href;
    }
  }, [href])

  let [scale] = useYcrState<number>("gui/scale")

  document.documentElement.style.fontSize = `${16 * scale}px`

  // constantly yield a dirty rect to fix https://github.com/chromiumembedded/cef/issues/3826
  useEffect(() => {
    function queueRecolor(color: string, nextColor: string) {
      setTimeout(() => {
        let div = document.getElementById("cef-fix")
        if (div !== null) div.style.backgroundColor = color
        queueRecolor(nextColor, color)
      }, 200)
    }

    queueRecolor("#00000000", "#00000001")
  }, [])

  return <div className={"w-screen h-screen absolute overflow-hidden"}>
    <div id={"cef-fix"} className={"absolute top-0 left-0 w-px h-px"}/>
    <Routes>
      <Route element={<></>} path={"/"}></Route>
      <Route element={<HudPage key={token}/>} path={"/hud"}/>
      <Route element={<ConfigScreen key={token}/>} path={"/screen/config"}/>
      <Route element={<LeapMenuScreen key={token}/>} path={"/screen/leap-menu"}/>
    </Routes>
  </div>
}
