import {afterEach, describe, expect, it, vi} from 'vitest';
import {tokenExpirado} from './token-expirado.util';

describe('tokenExpirado', () => {
  afterEach(() => vi.useRealTimers());

  it('distingue un JWT vigente de uno caducado', () => {
    vi.useFakeTimers();
    vi.setSystemTime(new Date('2026-09-18T12:00:00Z'));
    const token = (exp: number) => `cabecera.${btoa(JSON.stringify({exp}))}.firma`;

    expect(tokenExpirado(token(Date.now() / 1000 + 60))).toBe(false);
    expect(tokenExpirado(token(Date.now() / 1000))).toBe(true);
    expect(tokenExpirado('invalido')).toBe(true);
  });
});
