"use client";

import Aos from 'aos';
import 'aos/dist/aos.css';
import './home.css'
import { useEffect } from 'react';
import { Instagram, Mail, Phone } from 'lucide-react';
import { useTranslations } from 'next-intl';

export default function Home() {
  useEffect(() => {
    Aos.init();
  }, []);

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
        <div data-aos="fade-right" data-aos-duration="900" className="holographic-container">
          <div className="holographic-card">
            <h2>{t('appointmentButton')}</h2>
          </div>
        </div>
      </main>
      <footer data-aos="fade-up" data-aos-anchor-placement="center-bottom" data-aos-duration="900"  className="row-start-3 flex gap-[24px] flex-wrap items-center justify-center">
        <a className="flex items-center gap-2 hover:underline hover:underline-offset-4" 
          href="https://google.com" target="_blank" rel="noopener noreferrer"
        >
          <Instagram color='white' />{t('socials.instagram')}
        </a>
        <a className="flex items-center gap-2 hover:underline hover:underline-offset-4" 
          href="https://google.com" target="_blank" rel="noopener noreferrer"
        >
          <Phone color='white' />{t('socials.phone')}
        </a>
        <a className="flex items-center gap-2 hover:underline hover:underline-offset-4" 
          href="https://google.com" target="_blank" rel="noopener noreferrer"
        >
          <Mail color='white' />{t('socials.email')}
        </a>
      </footer>
    </div>
  );
}
