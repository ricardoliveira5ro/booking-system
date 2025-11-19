import './home.css'
import { Instagram, Mail, MoveRight, Phone } from 'lucide-react';
import { useTranslations } from 'next-intl';
import Link from 'next/link';

export default function Home() {

  const t = useTranslations('home');

  return (
    <div className="font-sans grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen px-4 pb-20 gap-16">
      <main className="flex flex-col gap-[32px] row-start-2 items-center">
        <h3 data-aos="fade-right" data-aos-duration="900" className="home-title">{t('title')}</h3>
        <div id="pole">
          <div className="dome top"></div>
          <div className="ring top"></div>
          <div id="middle">
            <div className="stripe red" id="a"></div>
            <div className="stripe blue" id="b"></div>
            <div className="stripe red" id="c"></div>
            <div className="stripe blue" id="d"></div>
          </div>
          <div className="dome bottom"></div>
          <div className="ring bottom"></div>
          <div id="shadow"></div>
        </div>
        <p data-aos="fade-right" data-aos-duration="900" className="px-4 text-center">{t('description')}</p>
        <div data-aos="fade-right" data-aos-duration="900" className='flex flex-col gap-y-5 items-center'>
          <div className="holographic-container">
            <Link href={'/appointment'} className="holographic-card cursor-pointer">
              <h2>{t('appointmentButton')}</h2>
            </Link>
          </div>
          <div className='relative group w-fit cursor-pointer p-0.5'>
            <div className="flex gap-x-1">
              <span>Login</span>
              <MoveRight />
            </div>
            <span className="absolute left-0 -bottom-[2px] h-[2px] w-0 bg-white transition-all duration-300 group-hover:w-full"></span>
          </div>
        </div>
      </main>
      <footer data-aos="fade-up" data-aos-anchor-placement="center-bottom" data-aos-duration="900"  className="row-start-3 flex gap-[24px] flex-wrap items-center justify-center">
        <a className="relative group flex items-center gap-2 pb-[2px]" 
          href="https://google.com" target="_blank" rel="noopener noreferrer"
        >
          <Instagram color='white' />
          <span className="relative after:absolute after:left-0 after:-bottom-[2px] after:h-[2px] after:w-0 after:bg-white after:transition-all after:duration-300 group-hover:after:w-full">
            {t('socials.instagram')}
          </span>
        </a>
        <a className="relative group flex items-center gap-2 pb-[2px]" 
          href="https://google.com" target="_blank" rel="noopener noreferrer"
        >
          <Phone color='white' />
          <span className="relative after:absolute after:left-0 after:-bottom-[2px] after:h-[2px] after:w-0 after:bg-white after:transition-all after:duration-300 group-hover:after:w-full">
            {t('socials.phone')}
          </span>
        </a>
        <a className="relative group flex items-center gap-2 pb-[2px]" 
          href="https://google.com" target="_blank" rel="noopener noreferrer"
        >
          <Mail color='white' />
          <span className="relative after:absolute after:left-0 after:-bottom-[2px] after:h-[2px] after:w-0 after:bg-white after:transition-all after:duration-300 group-hover:after:w-full">
            {t('socials.email')}
          </span>
        </a>
      </footer>
    </div>
  );
}
