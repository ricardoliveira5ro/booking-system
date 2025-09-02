import './home.css'

export default function Home() {
  return (
    <div className="font-sans flex items-center justify-items-center min-h-screen p-8 pb-20 gap-16">
      <main className="flex flex-col gap-[32px] items-center">
        <h3 className="home-title">Abel BarberShop</h3>
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
        <p className="px-4 text-center">Fresh cuts, good vibes, and no waiting around. Book your time and you’re set.</p>
        <div className="px-4">
          <button className="bg-[#FE5F55] px-3 py-1.5 rounded-lg font-sans">Book Your Appointment</button>
        </div>
      </main>
    </div>
  );
}
