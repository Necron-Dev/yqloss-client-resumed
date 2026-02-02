import ModuleCard from "@/pages/config/ModuleCard.tsx";
import {useYcrState} from "@/api.tsx";
import {ModuleInfo} from "@/pages/config/ModulesPage.tsx";

export default function ModulesContent({setInfo}: { setInfo: (module: ModuleInfo) => void }) {
  const [modules] = useYcrState<ModuleInfo[]>("modules/list")

  return <div className={"flex flex-col gap-2"}>
    {
      modules.map(info =>
        <ModuleCard key={`ModuleCard-${info.id}`} info={info} setInfo={setInfo}/>
      )
    }
  </div>
}
