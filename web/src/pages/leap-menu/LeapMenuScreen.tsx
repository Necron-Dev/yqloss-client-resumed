import Located, {at} from "@/Located.tsx";
import {motion} from "framer-motion";
import {HOST, useYcrState} from "@/api.tsx";

type Class = "archer" | "berserk" | "mage" | "tank" | "healer" | "unknown"

interface Player {
  name: string
  dead: boolean
  selectedClass: Class
}

function getPositionWithOffset([centerX, centerY]: [number, number], radius: number, angle: number, gap: number): [number, number] {
  angle += Math.asin(gap / radius)
  const px = centerX + radius * Math.cos(angle)
  const py = centerY + radius * Math.sin(angle)
  return [px, py]
}

const GAP = 0.01
const VIEWBOX = 35 / 30

const CLASS_NAMES: Record<Class, string> = {
  archer: "Archer",
  berserk: "Berserk",
  mage: "Mage",
  tank: "Tank",
  healer: "Healer",
  unknown: "???"
}

const FONT_SIZE = 0.1

export default function LeapMenuScreen() {
  const [players] = useYcrState<(Player | null)[]>("leap-menu/players")

  return <Located location={at(0.5, 0.5, 0.5, 0.5)}>
    <motion.div className={"w-140 h-140"} initial={{opacity: 0, scale: 0.85}} animate={{opacity: 1, scale: 1}}>
      {/*<div className={"absolute left-45 top-45 w-50 h-50 bg-background-secondary rounded-[10rem]"}>*/}
      {/*</div>*/}
      <motion.svg
        className={"absolute left-0 top-0"}
        width={"35rem"}
        height={"35rem"}
        viewBox={`${-VIEWBOX} ${-VIEWBOX} ${VIEWBOX * 2} ${VIEWBOX * 2}`}
      >
        {
          players.map((player, index) => {
            const clickable = player?.dead === false
            const ia = (index - 1) / players.length * Math.PI * 2 - Math.PI / 2
            const oa = index / players.length * Math.PI * 2 - Math.PI / 2
            const or = 1
            const ir = (player === null ? 113 : 50) / 120 + GAP * 2
            const [x1, y1] = getPositionWithOffset([0, 0], or, ia, GAP)
            const [x2, y2] = getPositionWithOffset([0, 0], or, oa, -GAP)
            const [x3, y3] = getPositionWithOffset([0, 0], ir, oa, -GAP)
            const [x4, y4] = getPositionWithOffset([0, 0], ir, ia, GAP)
            const [xm, ym] = getPositionWithOffset([0, 0], (or + ir) / 2, (ia + oa) / 2, 0)
            return <motion.g
              key={index}
              whileHover={clickable ? {scale: 1.035} : {}}
              whileTap={clickable ? {scale: 1.015} : {}}
              onClick={clickable ? () => {
                void fetch(`${HOST}module/leap-menu/leap-to`, {
                  method: "POST",
                  body: JSON.stringify(player.name)
                })
              } : undefined}
            >
              <motion.path
                className={player?.dead ? "fill-red-950" : "fill-background-secondary"}
                d={`
                  M ${x1} ${y1}
                  A ${or} ${or} 0 0 1 ${x2} ${y2}
                  L ${x3} ${y3}
                  A ${ir} ${ir} 0 0 0 ${x4} ${y4}
                  Z
                `}
              />
              {
                player && <>
                  <text
                    className={player.dead ? "fill-gray-500" : "fill-foreground"}
                    x={xm}
                    y={ym - FONT_SIZE / 2}
                    fontSize={FONT_SIZE}
                    textAnchor={"middle"}
                    dominantBaseline={"middle"}
                  >
                    {CLASS_NAMES[player.selectedClass]}
                  </text>
                  <text
                    className={player.dead ? "fill-gray-500" : "fill-foreground"}
                    x={xm}
                    y={ym + FONT_SIZE / 2}
                    fontSize={FONT_SIZE}
                    textAnchor={"middle"}
                    dominantBaseline={"middle"}
                  >
                    {player.name}
                  </text>
                </>
              }
            </motion.g>
          })
        }
      </motion.svg>
    </motion.div>
  </Located>
}
