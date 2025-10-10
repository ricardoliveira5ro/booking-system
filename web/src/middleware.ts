import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(req: NextRequest) {
  const acceptLanguage = req.headers.get('accept-language') || '';
  const raw = acceptLanguage.split(',')[0]?.split('-')[0]?.toLowerCase();
  const locale = raw === 'pt' ? 'pt' : 'en';

  const res = NextResponse.next();
  res.cookies.set('NEXT_LOCALE', locale, { path: '/' });

  return res;
}

export const config = {
  matcher: ['/((?!_next|.*\\..*).*)']
};