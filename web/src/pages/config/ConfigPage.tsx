import Located, {at} from "@/Located.tsx"
import {motion} from "framer-motion"
import {Card, Separator, Tabs} from "@heroui/react";
import ModulesSubpage from "@/pages/config/ModulesSubpage.tsx";

export default function ConfigPage() {
  return <Located location={at(0.5, 0.5, 0.5, 0.5)}>
    <motion.div initial={{scale: 0.85, opacity: 0}} animate={{scale: 1, opacity: 1}}>
      <Card className={"w-150 h-100"}>
        <Tabs className={"h-full"} orientation={"vertical"} variant={"secondary"}>
          <div className={"flex flex-col items-center"}>
            <span className={"font-bold mt-[0.82rem] mb-0.5 text-right leading-none"}>
              <span>Yqloss Client</span>
              <br/>
              <span className={"font-light text-[0.6rem] text-gray-300 border-white border rounded-xl pl-1 pr-1"}>
                Resumed
              </span>
            </span>
            <Tabs.ListContainer className={"p-2"}>
              <Tabs.List>
                <Tabs.Tab id={"modules"}>
                  Modules
                  <Tabs.Indicator/>
                </Tabs.Tab>
                <Tabs.Tab id={"profiles"}>
                  Profiles
                  <Tabs.Indicator/>
                </Tabs.Tab>
                <Tabs.Tab id={"settings"}>
                  Settings
                  <Tabs.Indicator/>
                </Tabs.Tab></Tabs.List>
            </Tabs.ListContainer>
          </div>
          <Separator orientation={"vertical"}/>
          <div className={"w-full h-full pb-10"}>
            <Tabs.Panel id={"modules"} className={"h-full"}>
              <h1 className={"font-semibold text-xl pb-3 ml-2"}>Modules</h1>
              <ModulesSubpage/>
            </Tabs.Panel>
            <Tabs.Panel id={"profiles"} className={"h-full"}>
              <h1 className={"font-semibold text-xl pb-3 ml-2"}>Profiles</h1>
            </Tabs.Panel>
            <Tabs.Panel id={"settings"} className={"h-full"}>
              <h1 className={"font-semibold text-xl pb-3 ml-2"}>Settings</h1>
            </Tabs.Panel>
          </div>
        </Tabs>
      </Card>
    </motion.div>
  </Located>
}
