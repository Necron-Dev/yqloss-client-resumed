import {Route, Routes} from "react-router-dom";
import HudPage from "@/pages/hud/HudPage.tsx";
import ConfigPage from "@/pages/config/ConfigPage.tsx";
import {useYcrState} from "@/api.tsx";
import {useEffect} from "react";

export default function App() {
  const [uuid] = useYcrState<string>("uuid")

  window.uuid = uuid

  let [href] = useYcrState<string>("href");

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
      <Route element={<HudPage/>} path={"/hud"}/>
      <Route element={<ConfigPage/>} path={"/screen/config"}/>
    </Routes>
  </div>
}
