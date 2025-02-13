-- Functions

-- if ORACLE

create or replace function QName(SG,BINSET_TO_STR)(BINSET in number, TRANSLATION_TABLE in nvarchar) return nvarchar is
    I        number := BINSET;
    INDX     binary_integer;
    T        nvarchar(32767) := TRANSLATION_TABLE;
    V        nvarchar(200);
    RESULT   nvarchar(32767);
begin

    while I > 0 and T is not null loop
        INDX := INSTR(T, ':');
        if INDX = 0 then
          V := T;
          T := null;
        else
          V := SUBSTR(T, 1, INDX-1);
          T := SUBSTR(T, INDX+1);
        end if;

        if BITAND(I,1) = 1 then
            if RESULT is null then
                RESULT := V;
            else
                RESULT := RESULT || ',' || V;
            end if;
        end if;
        I := FLOOR(I / 2);
    end loop;
    return RESULT;
end BINSET_TO_STR;

-- end