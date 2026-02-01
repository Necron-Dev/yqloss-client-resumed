import {ScrollShadow} from "@heroui/react";
import {useYcrState} from "@/api.tsx";
import ModuleCard from "@/pages/config/ModuleCard.tsx";

export default function ModulesSubpage() {
  const [modules] = useYcrState<{
    id: string,
    name: string,
    description: string
  }[]>("modules/list")

  return <ScrollShadow hideScrollBar className={"h-full rounded-2xl"} visibility={"none"}>
    <div className={"flex flex-col gap-2"}>
      {
        modules.map(({id, name, description}) =>
          <ModuleCard key={`module-${id}`} id={id} name={name} description={description}/>)
      }
    </div>
  </ScrollShadow>
}
