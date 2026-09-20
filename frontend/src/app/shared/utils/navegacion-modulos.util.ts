export function navegarModuloEnMismaPestana(): boolean {
  return window.matchMedia('(max-width: 1100px), (pointer: coarse)').matches;
}
