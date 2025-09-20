"use client";
import React from "react";

interface CircularStepProgressProps {
  currentStep: number;
  totalSteps: number;
  size?: number;
  strokeWidth?: number;
  color?: string;
  bgColor?: string;
}

const CircularStepProgress: React.FC<CircularStepProgressProps> = ({
  currentStep,
  totalSteps,
  size = 64,
  strokeWidth = 8,
  color = "var(--orange)",
  bgColor = "#e5e7eb",
}) => {
  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  const progress = Math.min(currentStep / totalSteps, 1);
  const offset = circumference - progress * circumference;
  const fontSize = Math.round(size * 0.28);

  return (
    <svg width={size} height={size} className="block">
      <circle
        cx={size / 2}
        cy={size / 2}
        r={radius}
        fill="transparent"
        stroke={bgColor}
        strokeWidth={strokeWidth}
      />
      <circle
        cx={size / 2}
        cy={size / 2}
        r={radius}
        fill="transparent"
        stroke={color}
        strokeWidth={strokeWidth}
        strokeLinecap="round"
        strokeDasharray={circumference}
        strokeDashoffset={offset}
        transform={`rotate(-90 ${size / 2} ${size / 2})`}
        style={{ transition: "stroke-dashoffset 500ms ease-in-out" }}
      />
      <text
        x="50%"
        y="50%"
        dominantBaseline="middle"
        textAnchor="middle"
        style={{
          fontSize,
          fontWeight: 600,
          fill: "white",
        }}
      >
        {`${currentStep}/${totalSteps}`}
      </text>
    </svg>
  );
};

export default CircularStepProgress;
