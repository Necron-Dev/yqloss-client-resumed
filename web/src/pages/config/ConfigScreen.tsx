import Located, {at} from "@/Located.tsx"
import {AnimatePresence, motion} from "framer-motion"
import {Button, Card, Separator, Tabs} from "@heroui/react";
import ModulesPage from "@/pages/config/ModulesPage.tsx";
import React, {useState} from "react";

const TITLES: Record<string, string> = {
  "modules": "Modules",
  "profiles": "Profiles",
  "settings": "Settings",
}

export default function ConfigScreen() {
  const [selection, setSelection] = useState<string>("modules")
  const [title, setTitle] = useState<string | undefined>(undefined)
  const [showGoBack, setShowGoBack] = useState(false)
  const [reload, setReload] = useState(false)

  const displayTitle = title ?? TITLES[selection]

  function setTitleOrDefault(title: string | undefined) {
    setTitle(title)
    setShowGoBack(title !== undefined)
  }

  return <Located location={at(0.5, 0.5, 0.5, 0.5)}>
    <motion.div initial={{scale: 0.85, opacity: 0}} animate={{scale: 1, opacity: 1}}>
      <Card className={"w-150 h-100"}>
        <Tabs
          className={"h-full"}
          orientation={"vertical"}
          variant={"secondary"}
          onSelectionChange={value => {
            if (selection === value) return
            setSelection(value as string)
            setTitleOrDefault(undefined)
          }}
        >
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
          <div className={"flex flex-col w-full h-full"}>
            <div className={"flex flex-row justify-between items-center ml-2 pb-2"}>
              <h1 className={"font-semibold text-xl mt-1 mb-1"}>{displayTitle}</h1>
              <AnimatePresence mode={"wait"}>
                {
                  showGoBack &&
                  <motion.div
                    key={"go-back"}
                    initial={{opacity: 0}}
                    animate={{opacity: 1}}
                    exit={{opacity: 0}}
                    transition={{duration: 0.1, ease: "easeOut"}}
                  >
                    <Button size={"sm"} variant={"ghost"} onClick={() => {
                      setTitleOrDefault(undefined)
                      setReload(!reload)
                    }}>Go back</Button>
                  </motion.div>
                }
              </AnimatePresence>
            </div>
            <AnimatePresence mode={"wait"}>
              <motion.div
                key={`${selection}-${reload}`}
                initial={{opacity: 0, y: "2rem"}}
                animate={{opacity: 1, y: 0}}
                exit={{opacity: 0, y: "2rem"}}
                transition={{duration: 0.1, ease: "easeOut"}}
                className={"flex-1 min-h-0"}
              >
                {(
                  {
                    "modules": () => <ModulesPage setTitle={setTitleOrDefault}/>,
                  } as Record<string, () => React.ReactNode>
                )[selection]?.()}
              </motion.div>
            </AnimatePresence>
          </div>
        </Tabs>
      </Card>
    </motion.div>
  </Located>
}
