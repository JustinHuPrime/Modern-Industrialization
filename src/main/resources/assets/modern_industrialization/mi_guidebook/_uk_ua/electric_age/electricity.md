---
navigation:
  title: "Електрика"
  icon: "modern_industrialization:lv_steam_turbine"
  position: 104
  parent: modern_industrialization:electric_age.md
item_ids:
  - modern_industrialization:lv_steam_turbine
  - modern_industrialization:lv_diesel_generator
  - modern_industrialization:lv_mv_transformer
  - modern_industrialization:mv_lv_transformer
---

# Електрика

Парова турбіна використовує пару для виробництва електроенергії. Він перетворює кожен mb пару в 1 EU, до 32 mb конвертується за кожен такт

<Recipe id="modern_industrialization:electric_age/machine/lv_steam_turbine_asbl" />

Парова турбіна автоматично надсилатиме електроенергію до будь-якої машини, підключеної безпосередньо до її вихідної сторони. Він підключатиметься лише до кабелів, розміщених на його вихідній стороні. Він має *рівень* низької напруги (НН).

Кожен електричний кабель має рівень, який визначає, скільки EU/т він може передати та до яких машин його можна підключити. Мідь, срібло та олово — НН, мідно-нікелевий сплав і елетрум — СН і так далі...

Дизельний генератор є альтернативою парової турбіни. Для виробництва електроенергії використовується різне паливо. Поки що ви можете спалити креозот. (Перелік спалюваного палива див. у REI)

<Recipe id="modern_industrialization:electric_age/machine/lv_diesel_generator_asbl" />

Кабельні мережі мають обмеження щодо кількості енергії, яку вони залучатимуть до мережі: щонайбільше 256 EU/т для кабелів низької напруги, 1024 EU/т для кабелів середньої напруги та 8192 EU/т для кабелів високої напруги. Оскільки для мережі немає обмежень на потужність і оскільки кабелі мають невелику внутрішню пам’ять, мережа низької напруги може забезпечити понад 256 EU/т протягом короткого проміжку часу.

Однак зауважте, що одноблочні електричні машини підключаються лише до кабелів низької напруги!

Щоб передати більше енергії, вам потрібно або створити кілька мереж труб, або використовувати трансформатори.

Трансформатор НВ в ВВ (наприклад, НН в СН) має 5 входів і 1 вихід. Трансформатор ВВ в НВ (наприклад, СН в НН) має 1 вхід і 5 виходів



<Recipe id="modern_industrialization:electric_age/transformer/lv_mv/up_asbl" />

<Recipe id="modern_industrialization:electric_age/transformer/lv_mv/down_asbl" />

