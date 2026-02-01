import {PropsWithChildren} from "react";

interface Props {
  location: {
    relativeX: number
    relativeY: number
    anchorX: number
    anchorY: number
    offsetX: number
    offsetY: number
  }
}

export function at(relativeX?: number | undefined,
                   relativeY?: number | undefined,
                   anchorX?: number | undefined,
                   anchorY?: number | undefined,
                   offsetX?: number | undefined,
                   offsetY?: number | undefined) {
  return {
    relativeX: relativeX ?? 0,
    relativeY: relativeY ?? 0,
    anchorX: anchorX ?? 0,
    anchorY: anchorY ?? 0,
    offsetX: offsetX ?? 0,
    offsetY: offsetY ?? 0,
  }
}

export default function Located({
                                  children,
                                  location: {
                                    relativeX,
                                    relativeY,
                                    anchorX,
                                    anchorY,
                                    offsetX,
                                    offsetY
                                  }
                                }: PropsWithChildren<Props>) {
  return <div
    style={{
      paddingLeft: `${relativeX * 100}vw`,
      paddingTop: `${relativeY * 100}vh`,
    }}
    className={"absolute flex"}
  >
    <div
      style={{
        transform: `translate(-${anchorX * 100}%, -${anchorY * 100}%) translate(${offsetX}rem, ${offsetY}rem)`,
      }}
      className={"absolute flex"}
    >
      {children}
    </div>
  </div>
}
