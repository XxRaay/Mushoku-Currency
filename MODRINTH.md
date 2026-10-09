# 🪙 Mushoku Currency

**Mushoku Currency** is a lore-accurate economic overhaul for Minecraft 1.21.1 (NeoForge & Fabric), faithfully recreating the regional economy and coinage system from the legendary universe of ***Mushoku Tensei: Jobless Reincarnation***.

Gone are the days where emeralds are the only currency everywhere. Different nations and continents now mint and trade in their own distinct historical currencies!

---

## 🌍 Regional Currencies & Coinage

Depending on the continent, climate, and biome, trade is conducted with drastically different currencies:

### 👑 Asura Kingdom (The Gold Standard)
The wealthiest human civilization in the world. Asuran coins represent the universal baseline for continental trade:
- **Asura Iron Coin** — Value: **1 unit**
- **Asura Copper Coin** — Value: **10 units** (10 Iron Coins)
- **Asura Large Copper Coin** — Value: **100 units** (10 Copper Coins / **Equivalent to 1 Vanilla Emerald**)
- **Asura Silver Coin** — Value: **1,000 units** (10 Large Copper Coins)
- **Asura Gold Coin** — Value: **10,000 units** (10 Silver Coins)

### 💀 Demon Continent (Tribal Scrap & Ore)
In the harsh lands of demons, gold and silver hold zero value. Currency consists of carved stone, scrap metal, and precious ore:
- **Demon Scrap Coin** — Value: **1 unit**
- **Demon Stone Coin** — Value: **10 units** (10 Scraps)
- **Demon Large Stone Coin** — Value: **100 units** (10 Stone Coins / **Equivalent to 1 Vanilla Emerald**)
- **Demon Green Ore Coin** — Value: **5,000 units** (50 Large Stone Coins)

### ✝️ Holy Land of Millis (Church Standard)
The theocratic realm maintains strict religious minting standards:
- **Millis Copper Coin** — Value: **10 units** (10 Copper = **Equivalent to 1 Vanilla Emerald**)
- **Millis Silver Coin** — Value: **1,000 units**
- **Millis Gold Coin** — Value: **10,000 units**

### 🏜️ Begaritt Continent (Caravan Trade)
The desert region of dungeons, trade caravans, and bazaar markets:
- **Begaritt Copper Coin** — Value: **10 units**
- **Begaritt Silver Coin** — Value: **1,000 units**
- **Begaritt Gold Coin** — Value: **10,000 units**

---

## 🏘️ Village Communities & Trade Overhaul

- **Procedural Community Clustering**: The world is divided into economic regions (~1024 blocks). Villages in the same cluster form recognized historical provinces (*Fittoa Province*, *Rikarisu Wastes*, *Holy Parish of Millishion*, *Rapan Trade Oasis*).
- **Automated Villager Conversion**: When opening a vanilla villager trade window, emerald-based offers automatically convert into the local region's native coins according to precise economic conversion rates.
- **Action Bar Alerts**: Receive ambient notifications indicating which currency is used when arriving at new settlements.

---

## 🔄 Quick Exchange & Crafting

- **In-Hand Quick Merging**: Hold `Shift + Right-Click` with a stack of 10 coins to instantly merge them into 1 coin of the next denomination.
- **In-Hand Quick Splitting**: Hold `Shift + Right-Click` with 1 high-value coin to split it into 10 lower-denomination coins.
- **Crafting Recipes**: Seamless shapeless crafting recipes are available in any crafting grid for easy denomination management.

---

## ⚔️ Adventurer's Guild Integration

When played alongside **Mushoku Guild**:
- **Localized Quest Bounties**: Contract rewards are distributed in native local currency rather than generic emeralds.
- **Guild Receptionist Banking**: Talk to Mira at the Guild desk to exchange emeralds, convert foreign currencies (with a canonical 5% guild commission), or exchange denominations.

---

## ⌨️ Commands

- `/currency balance` — View your total balance across all regional currencies in your inventory.
- `/currency village info` — Inspect current community details, province name, and currency type.
- `/currency village set <asura|demon|millis|begaritt> [name]` — (Admin) Manually set province currency and custom name.
- `/currency give <player> <region> <amount>` — (Admin) Give specified currency.

---

## 📦 Requirements & Compatibility

| Platform | Loader | Minecraft | Dependencies |
| :--- | :--- | :--- | :--- |
| **NeoForge** | NeoForge 21.1+ | `1.21.1` | [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Fabric** | Fabric Loader 0.16+ | `1.21.1` | [Fabric API](https://modrinth.com/mod/fabric-api), [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## 📜 License

Distributed under the **Fair Community & AI License (FCAIL v1.7)**. See the [LICENSE](https://github.com/XxRaay/Mushoku-Currency/blob/master/LICENSE) file or visit [FCAIL on GitHub](https://github.com/XxRaay/FCAIL) for details.
