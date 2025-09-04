import { getRequestConfig } from 'next-intl/server';
import { cookies } from 'next/headers';

export default getRequestConfig(async () => {
  const cookieStore = cookies();
  const detected = (await cookieStore).get('NEXT_LOCALE')?.value || 'en';

  const locale = detected === 'pt' ? 'pt' : 'en';

  return {
    locale: locale,
    messages: (await import(`../../public/locales/${locale}.json`)).default
  };
});