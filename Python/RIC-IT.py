def AlgoIT(A, p, s):
    cp = p
    cs = s
    start = True
    last_p = None
    st_p = []
    st_s = []

    while start or st_p:
        if start:
            if s <= p + 1:
                ret = 0
                start = False
                last_p = p
            else:
                q = Zt(A, cp, cs)
                st_s.append(cs)
                cs = q
        else:
            cs = st_s.pop()
            q = Zt(A, cp, cs)
            r = Zr(A, cp, cs)
            if last_p == cp:
                a = ret
                st_p.append(cp)
                cp = q
                cs = r
                start = True
            elif last_p == q:
                a = a - ret
                cp = r
                cs = st_s.pop()
                start = True
            else:
                a = a + ret
                ret = a + (r - q)
                start = False
                last_p = cp

    return ret
print