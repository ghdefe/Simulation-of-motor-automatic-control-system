clear
Ce = 0.192;
Tm = 0.075;
Ta = 0.00167;
p = 0.1;

Ts = 0.1;   %采样时间
Gs = tf(1/(Ce*p),[Tm*Ta,Tm,1]);
Gz = c2d(Gs,Ts,'ZOH');
z = tf('z',Ts);
pha = z^(-1);
Yz = pha/(1-z^(-1));
Dz = pha/(Gz*(1-pha));

[nums,dens] = tfdata(Gs,'v');
[numd,dend] = tfdata(Dz,'v');
[numg,deng] = tfdata(Gz,'v');
Gkz = Dz*Gz;
Gbz = feedback(Gkz,1);
Gu = zeros(100,1);
Ge = zeros(100,1);
Du = zeros(100,1);
De = zeros(100,1);
R = zeros(100,1);

for i=3:30
    if i>3
        R(i) = 1;
    end


    De(i) = R(i-1) - Gu(i-1);
    Du(i) = 0.9917*Du(i-1) + 0.0083*Du(i-2) + 0.026*De(i) - 0.0066*De(i-1);
    Ge(i-1) = Du(i);
    Gu(i) = 38.457*Ge(i-1) + 0.3177*Ge(i-2) + 0.2555*Gu(i-1);

end

y1 = step(Gbz);
y1 = y1(1:20); 
Du(3:10)
Gu(3:10)
