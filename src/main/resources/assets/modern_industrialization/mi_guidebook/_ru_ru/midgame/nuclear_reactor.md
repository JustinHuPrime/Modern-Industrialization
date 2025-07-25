---
navigation:
  title: "Ядерный реактор"
  icon: "modern_industrialization:uranium_fuel_rod_quad"
  position: 206
  parent: modern_industrialization:midgame.md
item_ids:
  - modern_industrialization:nuclear_reactor
  - modern_industrialization:nuclear_casing
  - modern_industrialization:nuclear_item_hatch
  - modern_industrialization:nuclear_fluid_hatch
---

# Ядерный реактор

Ядерный реактор - это большой мультиблок, предназначенный для выработки огромного количества энергии за счет потребления ядерного топлива.

<Recipe id="modern_industrialization:electric_age/machine/nuclear_reactor_asbl" />

Он может производить в 100 раз больше EU/t чем дизельное топливо. Он бывает разных размеров от маленького до большого.

То, как это работает под капотом, может быть немного ошеломляющим, даже несмотря на то, что на следующих страницах это пытаются объяснить.

Тем не менее, вам не нужно разбираться в деталях, чтобы спроектировать мощный реактор.

Мы рекомендуем вам поэкспериментировать с различными дизайнами в творческом режиме, пока вы не найдете тот, который вам подходит.

Обратите внимание, что ядерный реактор не может взорваться, испускать радиацию или иным образом повредить карту. Единственное, что он может повредить, - это предметы, которые вы кладете в него, если температура становится слишком высокой (подробнее об этом позже).

Вы можете свободно экспериментировать и наслаждаться новой жизнью ученого-ядерщика!

Основным компонентом является Ядерный Корпус, который изготовлен из ядерного сплава: смеси кадмия, бериллия и взрывоустойчивого сплава.

<Recipe id="modern_industrialization:electric_age/casing/nuclear_casing_asbl" />

Верхнюю часть конструкции составляют люки для ядерных предметов или жидкости (или простые корпуса). Это входы и выходы реактора.

<Recipe id="modern_industrialization:electric_age/casing/nuclear_item_hatch_asbl" />

Каждый люк имеет один вход (элемент или жидкость) и два выхода. Входные слоты образуют сетку, отображаемую в графическом интерфейсе реактора (доступном по щелчку правой кнопкой мыши на контроллере).

<Recipe id="modern_industrialization:electric_age/casing/nuclear_fluid_hatch_asbl" />

Каждый люк имеет температуру и накапливает тепло, которое можно отвести несколькими способами.
Тепло естественным образом переместится к соседнему люку или наружу, если люк находится на краю - тогда тепло теряется.
Скорость этого процесса равна коэффициенту теплопередачи содержимого люка (показанному в REI), умноженному на разницу температур.

Тепло также можно отводить в жидкостном люке, производя пар.

**Температура выше максимальной температуры предмета приведет к его разрушению!**

Элементами активной зоны ядерного реактора являются нейтроны.
Они производятся с помощью ядерного топлива.

Есть два типа нейтронов:
- быстрые;
- тепловые.
Быстрые переносят энергию, а тепловые - нет.
Нейтроны движутся по прямой линии, пока не встретят элемент или не выйдут из реактора (для быстрых нейтронов их энергия затем теряется).

Поток нейтронов отображается в графическом интерфейсе реактора.

Когда нейтрон встречает непустой люк, могут произойти две вещи: нейтрон поглощается или рассеивается.
Рассеянный нейтрон случайным образом изменит направление.

Если рассеянный нейтрон быстрый, он может замедлиться, превратившись в тепловой.

Все это приводит к передачи энергии от нейтрона к люку в виде тепла.

Поглощенный нейтрон останавливает своё движение и также передает свою энергию, как если бы это был быстрый нейтрон.

Количество поглощенных нейтронов на один люк можно увидеть в графическом интерфейсе пользователя.

Вероятность для каждого процесса показана в REI.
Она будет сильно зависеть от содержимого люка и типа нейтронов (быстрые или тепловые).

Ядерное топливо намного лучше поглощает тепловые нейтроны.

Когда нейтрон, быстрый или тепловой, поглощается ядерным топливом, генерируется больше нейтронов.

*Это всегда быстрые нейтроны со случайным направлением.*

Их генерация сопровождается прямым выделением энергии в виде дополнительного тепла в люке.

Выше определенного порога количество генерируемых нейтронов будет уменьшаться с температурой до нуля.
Этот процесс расходует немного энергии, но гарантирует стабильность реактора.

Количество генерируемых нейтронов (и эффективный КПД), прямая энергия и температурный порог указаны в REI.

Каждый ядерный компонент имеет максимальное количество поглощений. При достижении предмет либо уничтожается, либо превращается в истощенную версию.
Это особенно полезно для ядерного топлива, потому что некоторая часть U-238 превращается в плутоний в обедненной версии, что означает, что некоторая его часть может быть преобразована обратно в топливо.

То же самое происходит с жидкостями: немного жидкости трансформируется после каждого поглощения нейтронов. Это может быть использовано для массового производства полезных изотопов, таких как дейтерий и тритий.
В обоих случаях результат отображается в REI.

